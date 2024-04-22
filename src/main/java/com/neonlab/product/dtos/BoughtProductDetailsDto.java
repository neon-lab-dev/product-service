package com.neonlab.product.dtos;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Embeddable
public class BoughtProductDetailsDto {
    private String name;
    private String code;
    private BigDecimal price;
    private Integer quantity;
}
