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
import com.neonlab.common.utilities.AuthorizationUtil;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



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
        List<BoughtProductDetailsDto> boughtProductDetailsDtoList = new ArrayList<>();
        Order order = null;

        try {
            for (var productAndQty : orderDto.getProductCodeAndQty()) {
                Product product = productService.fetchProductByCode(productAndQty.getProductCode());
                Integer qty = productAndQty.getQuantity();
                int statusCode = reduceProductQuantity(qty, productAndQty.getProductCode());
                if (statusCode == 200) {
                    var boughtProductDetailsDto = ObjectMapperUtils.map(product, BoughtProductDetailsDto.class);
                    boughtProductDetailsDto.setPrice(product.getPrice());
                    boughtProductDetailsDto.setQuantity(qty);
                    List<Document> documents = documentService.fetchByDocIdentifierAndEntityName(product.getId(),
                            product.getClass().getSimpleName());

                    boughtProductDetailsDto.setDocumentsId(
                            documents.stream()
                                    .map(Document::getId)
                                    .toList()
                    );
                    boughtProductDetailsDtoList.add(boughtProductDetailsDto);
                }
            }

            if (!boughtProductDetailsDtoList.isEmpty()) {
                order = mapOrder(orderDto.getPaymentId(), boughtProductDetailsDtoList, shippingAddress);
            }

            var orderResponseDto = mapOrderResponse(boughtProductDetailsDtoList, shippingAddress, order);
            return new ApiOutput<>(HttpStatus.OK.value(), "Product Purchase Successful", orderResponseDto);
        } catch (NullPointerException e) {
            throw new NullPointerException("Product code or Quantity should not be null");
        }
    }


    private OrderResponseDto mapOrderResponse(List<BoughtProductDetailsDto> boughtProductDetailsDtoList, Address address, Order order) throws ServerException, InvalidInputException {
        var orderResponseDto = new OrderResponseDto();
        orderResponseDto.setBoughtProductDetailsDtoList(boughtProductDetailsDtoList);
        BigDecimal totalOrderedItemsPrice = BigDecimal.ZERO;
        for(var boughtProduct : boughtProductDetailsDtoList){
            BigDecimal price = boughtProduct.getPrice();
            BigDecimal productCost = price.multiply(BigDecimal.valueOf(boughtProduct.getQuantity()));
            totalOrderedItemsPrice = totalOrderedItemsPrice.add(productCost);
        }
        orderResponseDto.setOrderStatus(OrderStatus.PROCESSING);
        if(orderResponseDto.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            orderResponseDto.setDriverDetailsDto(mapDriverDetails());
        }
        var addressDto = ObjectMapperUtils.map(address,AddressDto.class);
        orderResponseDto.setShippingInfoDto(addressDto);
        orderResponseDto.setDeliveryCharge(getDeliveryCharge());
        orderResponseDto.setTotalOrderedItemsPrice(totalOrderedItemsPrice);
        orderResponseDto.setTotalPrice(totalOrderedItemsPrice.add(getDeliveryCharge()));
        var user = getUser();
        var userDetails = ObjectMapperUtils.map(user, UserDetailsDto.class);
        orderResponseDto.setUserDetailsDto(userDetails);
        orderResponseDto.setPaymentStatus("Success");// default
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setPaymentId(order.getPaymentId());
        orderResponseDto.setPaidDate(new Date());
        orderResponseDto.setDeliveredAt(getDefaultDeliveredAt());
        return orderResponseDto;
    }

    private Date getDefaultDeliveredAt() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); // Add 1 day to the current date
        return calendar.getTime();
    }
    private User getUser() throws InvalidInputException {
        var authUser = AuthorizationUtil.getCurrentUser();
        assert authUser != null;
        return userService.fetchById(authUser.getUserId());
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
        for(var boughtProduct : boughtProductDetailsDto) {
            order.setTotalItemCost(boughtProduct.getPrice());
            order.setTotalCost(boughtProduct.getPrice().add(getDeliveryCharge()));
        }
        order.setDeliveryCharges(getDeliveryCharge());
        order.setOrderStatus(OrderStatus.PROCESSING);
        var driverDetailsDto = mapDriverDetails();
        String driverDetails = JsonUtils.jsonOf(driverDetailsDto);
        order.setDriverDetails(driverDetails);
        order.setCreatedBy(userService.getLoggedInUser().getId());
        return orderRepository.save(order);
    }

    private DriverDetailsDto mapDriverDetails(){
        var driverDetailsDto = new DriverDetailsDto();
        driverDetailsDto.setDriverName("Raju");
        driverDetailsDto.setContactNo("9574413123");
        driverDetailsDto.setVehicleNo("JH1234");
        return driverDetailsDto;
    }

    private Integer reduceProductQuantity(Integer qty, String code) throws InvalidInputException, ServerException {
        var productDeleteReq = new ProductDeleteReq();
        productDeleteReq.setQuantity(qty);
        productDeleteReq.setCode(code);
        deleteProductApi.validate(productDeleteReq);
        return deleteProductApi.deleteProductApi(productDeleteReq).getStatusCode();
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
