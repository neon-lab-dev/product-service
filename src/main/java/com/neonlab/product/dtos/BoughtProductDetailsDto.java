package com.neonlab.product.dtos;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Data
@Embeddable
public class BoughtProductDetailsDto {
    private String code;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String variety;
    private List<String> documentsId = new ArrayList<>();
}
