package com.neonlab.product.dtos;
import lombok.Data;
import java.math.BigDecimal;


@Data
public class BoughtProductDetailsDto {
    private String name;
    private String code;
    private BigDecimal price;
    private Integer quantity;
}
