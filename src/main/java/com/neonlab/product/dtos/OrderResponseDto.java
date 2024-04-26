package com.neonlab.product.dtos;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.product.enums.OrderStatus;
import jakarta.persistence.Embedded;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



@Data
public class OrderResponseDto {

    private List<BoughtProductDetailsDto> boughtProductDetailsDtoList;
    private BigDecimal totalOrderedItemsPrice;
    private BigDecimal deliveryCharge;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private String paymentId;
    private String paymentStatus;
    private Date deliveredAt;
    private Date createdAt;
    private Date paidDate;
    @Embedded
    private UserDetailsDto userDetailsDto;
    @Embedded
    private AddressDto shippingInfoDto;
    @Embedded
    private DriverDetailsDto driverDetailsDto;
}
