package com.neonlab.product.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.product.enums.Units;
import com.neonlab.product.enums.VarietyType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VarietyDto {

    private String id;
    @NotNull(message = "Variety type is mandatory.")
    private VarietyType type;
    @NotNull(message = "Product value is mandatory.")
    private Integer value;
    @NotNull(message = "Product Unit is mandatory.")
    private Units unit;
    @NotEmpty(message = "Product specific description is mandatory.")
    private String description;
    @NotNull(message = "Product price is mandatory.")
    private BigDecimal price;
    private BigDecimal discountPercent;
    @NotNull(message = "Product quantity is mandatory.")
    private Integer quantity;
    private String productId;
    private List<MultipartFile> documents;
}
