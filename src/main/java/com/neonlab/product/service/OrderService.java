package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
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
        var address = addressService.save(orderDto.getShippingInfoDto(),getUser());
        List<BoughtProductDetailsDto> boughtProductDetailsDtoList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        try {
            for (String code : orderDto.getProductCodeAndQuantity().keySet()) {
                Product product = productService.fetchProductByCode(code);
                Integer qty = orderDto.getProductCodeAndQuantity().get(code);
                int statusCode = reduceProductQuantity(qty, code);
                if (statusCode == 200) {
                    var boughtProductDetailsDto = ObjectMapperUtils.map(product,BoughtProductDetailsDto.class);
                    boughtProductDetailsDto.setPrice(product.getPrice());
                    boughtProductDetailsDto.setQuantity(qty);
                    List<Document> documents = documentService.fetchByDocIdentifierAndEntityName(product.getId(),
                            product.getClass().getSimpleName());

                    boughtProductDetailsDto.setDocumentsId(
                            documents.stream()
                                    .map(Document::getId)
                                    .toList()
                    );
                    orderList.add(mapOrder(orderDto, boughtProductDetailsDto,address));
                    boughtProductDetailsDtoList.add(boughtProductDetailsDto);
                }
            }
            var orderResponseDto = mapOrderResponse(boughtProductDetailsDtoList,address,orderList);

            return new ApiOutput<> (HttpStatus.OK.value(), "Product Purchase Successful", orderResponseDto);
        }catch (NullPointerException e){
            throw new NullPointerException("Product code or Quantity should not be null");
        }
    }

    private OrderResponseDto mapOrderResponse(List<BoughtProductDetailsDto> boughtProductDetailsDtoList, Address address, List<Order> orderList) throws ServerException, InvalidInputException {
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
        for(var order : orderList) {
            orderResponseDto.setCreatedAt(order.getCreatedAt());
            orderResponseDto.setPaymentId(order.getPaymentId());
        }
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

    private Order mapOrder(OrderDto orderDto, BoughtProductDetailsDto boughtProductDetailsDto, Address address) throws InvalidInputException, ServerException {
        var order = new Order();
        var user = getUser();
        order.setUser(user);
        order.setAddressId(address.getId());
        order.setPaymentId(orderDto.getPaymentId());
        String boughtProductDetails = JsonUtils.jsonOf(boughtProductDetailsDto);
        order.setBoughtProductDetails(boughtProductDetails);
        order.setTotalItemCost(boughtProductDetailsDto.getPrice());
        order.setDeliveryCharges(getDeliveryCharge());
        order.setTotalCost(boughtProductDetailsDto.getPrice().add(getDeliveryCharge()));
        var driverDetailsDto = mapDriverDetails();
        String driverDetails = JsonUtils.jsonOf(driverDetailsDto);
        order.setDriverDetails(driverDetails);
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
        return BigDecimal.valueOf(0);
    }
}
