package com.neonlab.product.dtos;
import com.neonlab.common.dto.DocumentDto;
import com.neonlab.product.enums.Units;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDto extends DocumentDto {
    private String name;
    private String category;
    private String subCategory;
    private String brand;
    private String code;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Units units;
    private String variety;
    private Integer quantity;
    private String description;
}
