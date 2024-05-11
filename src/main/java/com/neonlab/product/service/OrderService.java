package com.neonlab.product.service;
import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.config.ConfigurationKeys;
import com.neonlab.common.constants.GlobalConstants;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.UserDto;
import com.neonlab.common.entities.Address;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.services.SystemConfigService;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.PageableResponse;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.*;
import com.neonlab.common.entities.Order;
import com.neonlab.product.models.requests.UpdateOrderRequest;
import com.neonlab.product.models.searchCriteria.OrderSearchCriteria;
import com.neonlab.product.repository.OrderRepository;
import com.neonlab.product.repository.specifications.OrderSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

import static com.neonlab.common.config.ConfigurationKeys.CANCELABLE_PERIOD;


@Slf4j
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
            variety.setQuantity(variety.getQuantity() - boughtProduct.getBoughtQuantity());
            productService.saveVariety(variety);
        }
        setDeliveryCharge(orderDto);
        orderDto.setup();
        var order = orderDto.parseToEntity();
        order = orderRepository.save(order);
        orderDto = OrderDto.parse(order);
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
        if (Objects.nonNull(orderDto.getDriverDetailsDto()) && !StringUtil.isNullOrEmpty(orderDto.getDriverDetailsDto().getId())){
            var driver = driverService.fetchById(orderDto.getDriverDetailsDto().getId());
            var driverDto = ObjectMapperUtils.map(driver, DriverDto.class);
            orderDto.setDriverDetailsDto(driverDto);
        }
    }

    private void setDeliveryCharge(OrderDto orderDto) throws InvalidInputException {
        var config = systemConfigService.getSystemConfig(ConfigurationKeys.DELIVERY_CHARGE);
        orderDto.setDeliveryCharges(BigDecimal.valueOf(Integer.parseInt(config.getValue())));
    }

    public OrderDto update(UpdateOrderRequest request) throws InvalidInputException, ServerException, JsonParseException {
        var order = fetchById(request.getId());
        ObjectMapperUtils.map(request, order);
        order = orderRepository.save(order);
        var retVal = OrderDto.parse(order);
        setUpDtos(retVal);
       return retVal;
    }


    public boolean canOrderCancel(String id) throws InvalidInputException {
        var order = fetchById(id);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getCreatedAt());
        var cancelablePeriod = Integer.parseInt(systemConfigService.getSystemConfig(CANCELABLE_PERIOD).getValue());
        calendar.add(Calendar.DAY_OF_YEAR, cancelablePeriod);
        return !now.after(calendar.getTime());
    }

    public Order fetchById(String orderId) throws InvalidInputException {
       return orderRepository.findById(orderId).
                orElseThrow(()->new InvalidInputException("Order not found with id "+orderId));
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
        for (var boughtProduct : orderDto.getBoughtProductDetailsList()){
            var variety = productService.fetchVarietyById(boughtProduct.getVarietyId());
            if (boughtProduct.getBoughtQuantity() > variety.getQuantity()){
                throw new InvalidInputException(
                        String.format("Bought quantity cannot be more than Product available stock." +
                                " Bought quantity is %d and available stock is %d",
                                boughtProduct.getBoughtQuantity(), variety.getQuantity()));
            }
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
                    String.format("Address Id %s does not belong to user Id %s", shippingAddressId, user.getId())
            );
        }
    }

    public PageableResponse<OrderDto> fetch(OrderSearchCriteria searchCriteria) throws InvalidInputException {
        var pageable = PageableUtils.createPageable(searchCriteria);
        var userId = getUserIdByAdmin(searchCriteria);
        searchCriteria.setUserId(userId);
        Page<Order> orders = orderRepository.findAll(
                OrderSpecifications.buildSearchCriteria(searchCriteria),
                pageable
        );
        var resultList = orders.getContent().stream()
                .map(order -> {
                    try {
                        var retVal = OrderDto.parse(order);
                        setUpDtos(retVal);
                        return retVal;
                    } catch (JsonParseException | InvalidInputException | ServerException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
        return new PageableResponse<>(resultList, searchCriteria);
    }

    private String getUserIdByAdmin(OrderSearchCriteria searchCriteria){
        String retVal = null;
        try {
            if (searchCriteria.isAdmin()){
                if (!StringUtil.isNullOrEmpty(searchCriteria.getPrimaryPhoneNo())){
                    retVal = userService.fetchByPrimaryPhoneNo(searchCriteria.getPrimaryPhoneNo()).getId();
                }
            } else {
                retVal = userService.getLoggedInUser().getId();
            }
        } catch (InvalidInputException e) {
            log.warn(GlobalConstants.ERROR_OCCURRED, e.getMessage());
        }
        return retVal;
    }

}
