package com.neonlab.product.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductCodeAndQtyDto {
    @NotNull(message = "Product code should not be null")
    @NotEmpty(message = "Product code should not be empty")
    private String productCode;

    @NotNull(message = "Quantity should not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
