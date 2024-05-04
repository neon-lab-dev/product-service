package com.neonlab.product.dtos;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.UserDto;
import com.neonlab.common.entities.Order;
import com.neonlab.common.enums.OrderStatus;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.common.utilities.MathUtils;
import com.neonlab.common.utilities.StringUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Data
@Slf4j
public class OrderDto {

    @NotEmpty(message = "Payment id should not be empty")
    private String paymentId;
    @Valid
    private List<BoughtProductDetailsDto> boughtProductDetailsList;
    private BigDecimal totalItemCost;
    private BigDecimal deliveryCharges;
    private BigDecimal totalCost;
    private OrderStatus orderStatus;
    private Date deliveredAt;
    private Date createdAt;
    private Date paidDate;
    private UserDto userDetailsDto;
    private AddressDto shippingInfo;
    private DriverDto driverDetailsDto;

    public void setup(){
        var allDiscountedPrice = this.boughtProductDetailsList.stream()
                .map(BoughtProductDetailsDto::getDiscountedPrice)
                .toList();
        this.totalItemCost = MathUtils.getSum(allDiscountedPrice);
        this.totalCost = this.totalItemCost.add(this.deliveryCharges);
        this.orderStatus = OrderStatus.PACKAGING;
        this.createdAt = new Date();
        this.paidDate = new Date();
    }

    public void setUserId(String id){
        var userDto = this.userDetailsDto;
        if (Objects.isNull(userDto)){
            userDto = new UserDto();
        }
        userDto.setId(id);
        this.userDetailsDto = userDto;
    }

    public void setAddressId(String id){
        var addressDto = this.shippingInfo;
        if (Objects.isNull(addressDto)){
            addressDto = new AddressDto();
        }
        addressDto.setId(id);
        this.shippingInfo = addressDto;
    }

    public void setDriverId(String id){
        if (!StringUtil.isNullOrEmpty(id)) {
            var driverDto = this.driverDetailsDto;
            if (Objects.isNull(driverDto)){
                driverDto = new DriverDto();
            }
            driverDto.setId(id);
            this.driverDetailsDto = driverDto;
        }
    }

    private static ModelMapper entityMapper = new ModelMapper();

    static {
        entityMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public Order parseToEntity(){
        TypeMap<OrderDto, Order> propertyMapper = entityMapper.createTypeMap(OrderDto.class, Order.class);
        propertyMapper
                .addMapping(orderDto -> orderDto.getUserDetailsDto().getId(), Order::setUserId)
                .addMapping(orderDto -> orderDto.getShippingInfo().getId(), Order::setAddressId)
                .addMapping(orderDto -> {
                    if (Objects.nonNull(orderDto.getDriverDetailsDto())){
                        return orderDto.getDriverDetailsDto().getId();
                    } else {
                        return null;
                    }
                }, Order::setDriverId)
        ;
        return entityMapper.map(this, Order.class);
    }

    public static OrderDto parse(Order currentOrder){
        return entityMapper.map(currentOrder, OrderDto.class);
    }

}
