package com.neonlab.product.DTO;
import com.neonlab.product.enums.Units;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ProductResponseDTO {
    private String name;
    private String category;
    private String subCategory;
    private String code;
    private Integer price;
    private Integer discountPrice;
    @Enumerated(EnumType.STRING)
    private Units units;
    private String variety;
    private Integer quantity;
    private String tags;

}
