package com.neonlab.product.dtos;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.transaction.NoTransactionException;

import java.util.HashMap;
import java.util.Map;


@Data
public class OrderDto {
    @NotNull(message = "Payment id should not be null")
    private String paymentId;

    @NotNull(message = "Product code and Quantity should not be Null")
    @NotEmpty(message = "Product code and Quantity should not be Empty")
    private Map<String,Integer> productCodeAndQuantity;

    @NotNull(message = "Address Id should not be null")
    private String addressId;
}
