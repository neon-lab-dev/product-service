package com.neonlab.product.dtos;
import com.neonlab.common.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class OrderDto {
    @NotNull(message = "Payment id should not be null")
    @NotEmpty(message = "Payment id should not be empty")
    private String paymentId;

    @Valid
    private List<ProductCodeAndQtyDto> productCodeAndQty;

    @Valid
    private AddressDto shippingInfo;
}
