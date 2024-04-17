package com.neonlab.product.dtos;
import com.neonlab.product.enums.Units;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {
    private String name;
    private String category;
    private String subCategory;
    private String brand;
    private String code;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private Units units;
    private String variety;
    private Integer quantity;
    private String description;
    private List<String> documentId;
}
