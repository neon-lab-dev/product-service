package com.neonlab.product.dtos;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Data
public class BoughtProductDetailsDto {

    @NotEmpty(message = "Product code should not be empty")
    private String code;

    private String name;
    private BigDecimal price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String variety;
    private List<String> documentsId = new ArrayList<>();
}
