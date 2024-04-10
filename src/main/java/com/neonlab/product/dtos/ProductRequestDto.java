package com.neonlab.product.dtos;

import com.neonlab.product.enums.Units;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String name;
    private String category;
    private String subCategory;
    private String description;
    private String brand;
    private String code;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Units units;
    private String variety;
    private Integer quantity;
    private String tags;
}
