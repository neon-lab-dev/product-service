package com.neonlab.product.dtos;
import com.neonlab.common.utilities.MathUtils;
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
    private BigDecimal boughtPrice;
    private BigDecimal savings;
    private List<String> documents = new ArrayList<>();

    public void setup(){
        this.boughtPrice = this.discountedPrice.multiply(BigDecimal.valueOf(this.boughtQuantity)).setScale(MathUtils.DEFAULT_SCALE);
        this.savings = (this.price.subtract(this.discountedPrice)).multiply(BigDecimal.valueOf(this.boughtQuantity)).setScale(MathUtils.DEFAULT_SCALE);
    }

}
