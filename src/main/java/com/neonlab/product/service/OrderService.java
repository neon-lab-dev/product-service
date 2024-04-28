package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.dto.AuthenticationRequest;
import com.neonlab.common.entities.Address;
import com.neonlab.common.entities.Document;
import com.neonlab.common.entities.User;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.dtos.*;
import com.neonlab.product.entities.Order;
import com.neonlab.product.entities.Product;
import com.neonlab.product.enums.OrderStatus;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;


@Service
@Loggable
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeleteProductApi deleteProductApi;
    @Autowired
    private AddressService addressService;
    @Autowired
    private DocumentService documentService;


    @Transactional
    public ApiOutput<?> createOrder(OrderDto orderDto) throws InvalidInputException, ServerException {
        Address shippingAddress = addressService.save(orderDto.getShippingInfo(), getUser());
        List<BoughtProductDetailsDto> boughtProductsDetailsList = new ArrayList<>();
        Order order = null;

        try {
            for (var products : orderDto.getBoughtProductDetailsList()) {
                Product product = productService.fetchProductByCode(products.getCode());
                Integer qty = products.getQuantity();
                var status = reduceProductQuantity(qty, products.getCode());
                if (status) {
                    var boughtProductDetails = ObjectMapperUtils.map(product, BoughtProductDetailsDto.class);
                    boughtProductDetails.setQuantity(qty);
                    List<Document> documents = documentService.fetchByDocIdentifierAndEntityName(product.getId(),
                            product.getClass().getSimpleName());

                    boughtProductDetails.setDocumentsId(
                            documents.stream()
                                    .map(Document::getId)
                                    .toList()
                    );
                    boughtProductsDetailsList.add(boughtProductDetails);
                }
            }

            if (!boughtProductsDetailsList.isEmpty()) {
                order = mapOrder(orderDto.getPaymentId(), boughtProductsDetailsList, shippingAddress);
            }
            var orderResponseDto = mapOrderResponse(boughtProductsDetailsList, shippingAddress, order);
            return new ApiOutput<>(HttpStatus.OK.value(), "Product Purchase Successful", orderResponseDto);
        } catch (NullPointerException e) {
            throw new NullPointerException("Product code or Quantity should not be null");
        }
    }


    private OrderDto mapOrderResponse(List<BoughtProductDetailsDto> boughtProductDetailsDtoList, Address address, Order order) throws ServerException, InvalidInputException {
        var orderResponse = new OrderDto();
        orderResponse.setBoughtProductDetailsList(boughtProductDetailsDtoList);
        BigDecimal totalOrderedItemsPrice = getTotalOrderedItemsPrice(boughtProductDetailsDtoList);
        orderResponse.setOrderStatus(OrderStatus.PROCESSING);

        var addressDto = ObjectMapperUtils.map(address,AddressDto.class);
        orderResponse.setShippingInfo(addressDto);
        orderResponse.setDeliveryCharge(getDeliveryCharge());
        orderResponse.setTotalOrderedItemsPrice(totalOrderedItemsPrice);
        orderResponse.setTotalPrice(totalOrderedItemsPrice.add(getDeliveryCharge()));
        var user = userService.getLoggedInUser();
        var userDetails = ObjectMapperUtils.map(user, UserDetailsDto.class);
        orderResponse.setUserDetailsDto(userDetails);
        orderResponse.setPaymentStatus("Success");// default
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setPaymentId(order.getPaymentId());
        orderResponse.setPaidDate(new Date());
        orderResponse.setDeliveredAt(getDefaultDeliveredAt());
        return orderResponse;
    }

    private BigDecimal getTotalOrderedItemsPrice(List<BoughtProductDetailsDto> boughtProductDetailsDtoList) {
        BigDecimal totalOrderedItemsPrice = BigDecimal.ZERO;
        for(var boughtProduct : boughtProductDetailsDtoList){
            BigDecimal price = boughtProduct.getPrice();
            BigDecimal productCost = price.multiply(BigDecimal.valueOf(boughtProduct.getQuantity()));
            totalOrderedItemsPrice = totalOrderedItemsPrice.add(productCost);
        }
        return totalOrderedItemsPrice;
    }

    private Date getDefaultDeliveredAt() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); // Add 1 day to the current date
        return calendar.getTime();
    }
    private User getUser() throws InvalidInputException {
        return userService.getLoggedInUser();
    }

    private Order mapOrder(String paymentId, List<BoughtProductDetailsDto> boughtProductDetailsDto, Address address) throws InvalidInputException, ServerException {
        AuthenticationRequest request = new AuthenticationRequest();
        var order = new Order(request.getPhone(),request.getPhone());
        var user = getUser();
        order.setUser(user);
        order.setAddressId(address.getId());
        order.setPaymentId(paymentId);
        String boughtProductDetails = JsonUtils.jsonOf(boughtProductDetailsDto);
        order.setBoughtProductDetails(boughtProductDetails);
        BigDecimal totalOrderedItemsPrice = getTotalOrderedItemsPrice(boughtProductDetailsDto);
        order.setTotalItemCost(totalOrderedItemsPrice);
        order.setTotalCost(totalOrderedItemsPrice.add(getDeliveryCharge()));
        order.setDeliveryCharges(getDeliveryCharge());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setDriverId(null);
        order.setCreatedBy(userService.getLoggedInUser().getId());
        return orderRepository.save(order);
    }

    private Boolean reduceProductQuantity(Integer qty, String code) throws InvalidInputException, ServerException {
        var productDeleteReq = new ProductDeleteReq();
        productDeleteReq.setQuantity(qty);
        productDeleteReq.setCode(code);
        deleteProductApi.validate(productDeleteReq);
        return Optional.ofNullable((Boolean) deleteProductApi.process(productDeleteReq).getResponseBody()).orElse(false);
    }

    private BigDecimal getDeliveryCharge(){
        return BigDecimal.ZERO;
    }

    public ApiOutput<?> updateOrder(String orderId,String orderStatus) throws InvalidInputException, ServerException {
        var existOrder = fetchOrderById(orderId);
            existOrder.setOrderStatus(OrderStatus.fromString(orderStatus));
            orderRepository.save(existOrder);
       return new ApiOutput<>(HttpStatus.OK.value(), "Order Status Change",existOrder.getOrderStatus());
    }


    public ApiOutput<?> cancelOrder(String orderId) throws InvalidInputException {
        var order = fetchOrderById(orderId);
        orderRepository.delete(order);
        return new ApiOutput<>(HttpStatus.OK.value(), "Your Order Cancel Successfully",null);
    }

    private Order fetchOrderById(String orderId) throws InvalidInputException {
       return orderRepository.findById(orderId).
                orElseThrow(()->new InvalidInputException("Not Found Order Details with this id "+orderId));
    }
}
