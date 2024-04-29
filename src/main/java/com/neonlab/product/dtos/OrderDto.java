package com.neonlab.product.dtos;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.product.enums.OrderStatus;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class OrderDto {

    @NotEmpty(message = "Payment id should not be empty")
    private String paymentId;

    @Valid
    private List<BoughtProductDetailsDto> boughtProductDetailsList;
    private BigDecimal totalOrderedItemsPrice;
    private BigDecimal deliveryCharge;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private String paymentStatus;
    private Date deliveredAt;
    private Date createdAt;
    private Date paidDate;
    private UserDetailsDto userDetailsDto;
    @Valid
    private AddressDto shippingInfo;
    private DriverDetailsDto driverDetailsDto;
}
