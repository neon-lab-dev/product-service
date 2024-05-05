package com.neonlab.product.service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.config.ConfigurationKeys;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.dto.AuthenticationRequest;
import com.neonlab.common.dto.UserDto;
import com.neonlab.common.entities.Address;
import com.neonlab.common.entities.User;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.services.SystemConfigService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.*;
import com.neonlab.common.entities.Order;
import com.neonlab.common.enums.OrderStatus;
import com.neonlab.product.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;


@Service
@Loggable
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final DriverService driverService;
    private final UserService userService;
    private final AddressService addressService;
    private final SystemConfigService systemConfigService;

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) throws InvalidInputException, ServerException, JsonParseException {
        for (var boughtProduct : orderDto.getBoughtProductDetailsList()){
            var variety = productService.fetchVarietyById(boughtProduct.getVarietyId());
            var productVarietyResponse = productService.fetchProductVarietyResponse(variety);
            ObjectMapperUtils.map(productVarietyResponse, boughtProduct);
        }
        setDeliveryCharge(orderDto);
        orderDto.setup();
        var order = orderDto.parseToEntity();
        var boughtProducts = JsonUtils.jsonOf(orderDto.getBoughtProductDetailsList());
        order.setBoughtProductDetails(boughtProducts);
        order = orderRepository.save(order);
        orderDto = OrderDto.parse(order);
        var boughtProductDetails = JsonUtils.readObjectFromJson(order.getBoughtProductDetails(), new TypeReference<List<BoughtProductDetailsDto>>() {});
        orderDto.setBoughtProductDetailsList(boughtProductDetails);
        setUpDtos(orderDto);
        return orderDto;
    }

    private void setUpDtos(OrderDto orderDto) throws InvalidInputException, ServerException {
        setupUserDto(orderDto);
        setupAddressDto(orderDto);
        setupDriverDto(orderDto);
    }

    private void setupUserDto(OrderDto orderDto) throws InvalidInputException, ServerException {
        var user = userService.fetchById(orderDto.getUserDetailsDto().getId());
        var userDetails = ObjectMapperUtils.map(user, UserDto.class);
        orderDto.setUserDetailsDto(userDetails);
    }

    private void setupAddressDto(OrderDto orderDto) throws InvalidInputException, ServerException {
        var address = addressService.fetchById(orderDto.getShippingInfo().getId());
        var addressDto = ObjectMapperUtils.map(address, AddressDto.class);
        orderDto.setShippingInfo(addressDto);
    }

    private void setupDriverDto(OrderDto orderDto) throws InvalidInputException, ServerException {
        if (Objects.nonNull(orderDto.getDriverDetailsDto()) && StringUtil.isNullOrEmpty(orderDto.getDriverDetailsDto().getId())){
            var driver = driverService.fetchById(orderDto.getDriverDetailsDto().getId());
            var driverDto = ObjectMapperUtils.map(driver, DriverDto.class);
            orderDto.setDriverDetailsDto(driverDto);
        }
    }

    private void setDeliveryCharge(OrderDto orderDto) throws InvalidInputException {
        var config = systemConfigService.getSystemConfig(ConfigurationKeys.DELIVERY_CHARGE);
        orderDto.setDeliveryCharges(BigDecimal.valueOf(Integer.parseInt(config.getValue())));
    }

    private OrderDto mapOrderResponse(List<BoughtProductDetailsDto> boughtProductDetailsDtoList, Address address, Order order) throws ServerException, InvalidInputException {
        var orderResponse = new OrderDto();
        orderResponse.setBoughtProductDetailsList(boughtProductDetailsDtoList);
        BigDecimal totalOrderedItemsPrice = getTotalOrderedItemsPrice(boughtProductDetailsDtoList);
        orderResponse.setOrderStatus(OrderStatus.PROCESSING);

        var addressDto = ObjectMapperUtils.map(address,AddressDto.class);
        orderResponse.setShippingInfo(addressDto);
        //orderResponse.setDeliveryCharge(getDeliveryCharge());
        orderResponse.setTotalItemCost(totalOrderedItemsPrice);
        orderResponse.setTotalCost(totalOrderedItemsPrice.add(getDeliveryCharge()));
        var user = userService.getLoggedInUser();
        //var userDetails = ObjectMapperUtils.map(user, UserDetailsDto.class);
        //orderResponse.setUserDetailsDto(userDetails);
        orderResponse.setPaymentId("Success");// default
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
            BigDecimal productCost = price.multiply(BigDecimal.valueOf(boughtProduct.getBoughtQuantity()));
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
        order.setUserId(user.getId());
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
        order.setCreatedBy(getUser().getId());
        return orderRepository.save(order);
    }

    private Boolean reduceProductQuantity(Integer qty, String code) throws InvalidInputException, ServerException {
        /*var productDeleteReq = new ProductDeleteReq();
        productDeleteReq.setQuantity(qty);
        productDeleteReq.setCode(code);
        deleteProductApi.validate(productDeleteReq);
        return Optional.ofNullable((Boolean) deleteProductApi.process(productDeleteReq).getResponseBody()).orElse(false);*/
        return true;
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

    @Transactional
    public String cancelById(String orderId) throws InvalidInputException, JsonProcessingException {

        var order = fetchOrderById(orderId);
        productService.handleCancelOrder(order);
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return "Your Order Cancel Successfully";
    }


    public boolean canOrderCancel(String id) throws InvalidInputException {

        var order = fetchOrderById(id);
        if (order.getOrderStatus().getOrderStatus().equals("OUT_FOR_DELIVERY")) {
            return false;
        }
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getCreatedAt());
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return !now.after(calendar.getTime());
    }

    private Order fetchOrderById(String orderId) throws InvalidInputException {
       return orderRepository.findById(orderId).
                orElseThrow(()->new InvalidInputException("Not Found Order Details with this id "+orderId));
    }

    /**
     * Set of validations need to be done before order can be created.
     *
     * @param orderDto giver order request
     * @throws InvalidInputException in case any of the validation is not satisfied
     */
    public void createOrderValidations(OrderDto orderDto) throws InvalidInputException {
        validateVarietyIds(orderDto);
        validateUserAndAddress(orderDto);
    }

    private void validateVarietyIds(OrderDto orderDto) throws InvalidInputException {
        var ids = orderDto.getBoughtProductDetailsList().stream()
                .map(BoughtProductDetailsDto::getVarietyId)
                .toList();
        for (var id : ids){
            productService.fetchVarietyById(id);
        }
    }

    private void validateUserAndAddress(OrderDto orderDto) throws InvalidInputException {
        var user = userService.fetchById(orderDto.getUserDetailsDto().getId());
        var userAddresses = user.getAddresses().stream()
                .map(Address::getId)
                .toList();
        var shippingAddressId = orderDto.getShippingInfo().getId();
        var isUserAddress = userAddresses.contains(shippingAddressId);
        if (!isUserAddress){
            throw new InvalidInputException(
                    String.format("Address Id %s does not belong to user Id %s", user.getId(), shippingAddressId)
            );
        }
    }

}
