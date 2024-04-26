package com.neonlab.product.dtos;
import com.neonlab.common.dto.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.transaction.NoTransactionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class OrderDto {
    @NotNull(message = "Payment id should not be null")
    @NotEmpty(message = "Payment id should not be empty")
    private String paymentId;

    @NotNull(message = "Product code and Quantity map should not be null")
    @NotEmpty(message = "Product code and Quantity map should not be empty")
    private Map<@NotEmpty(message = "Product code should not be empty") String,
            @Min(value = 1, message = "Quantity must be at least 1") Integer> productCodeAndQuantity;

    @Valid
    private AddressDto shippingInfoDto;
}
