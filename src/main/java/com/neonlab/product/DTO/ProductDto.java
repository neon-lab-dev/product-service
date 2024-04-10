package com.neonlab.product.DTO;
import com.neonlab.product.entities.Product;
import com.neonlab.product.enums.Units;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private String name;
    private String category;
    private String subCategory;
    private String brand;
    private String code;
    private BigDecimal price;
    private BigDecimal discountPrice;
    @Enumerated(EnumType.STRING)
    private Units units;
    private String variety;
    private Integer quantity;

    public static ProductDto parse(Product product){
        var mapper = new ModelMapper();
        return  mapper.map(product, ProductDto.class);
    }
}
