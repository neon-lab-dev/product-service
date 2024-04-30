package com.neonlab.product.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.product.enums.Units;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    @NotEmpty(message = "Product name is mandatory.")
    private String name;
    @NotEmpty(message = "Product category is mandatory.")
    private String category;
    @NotEmpty(message = "Product sub-category is mandatory.")
    private String subCategory;
    private String brand;
    private List<VarietyDto> varieties;
}
