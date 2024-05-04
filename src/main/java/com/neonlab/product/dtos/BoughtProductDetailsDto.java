package com.neonlab.product.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Data
public class BoughtProductDetailsDto {

    @NotEmpty(message = "Product variety Id is mandatory.")
    private String varietyId;
    private String name;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private BigDecimal discountedPrice;
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer boughtQuantity;
    private List<String> documents = new ArrayList<>();
}
