package com.neonlab.product.models.responses;

import com.neonlab.common.utilities.MathUtils;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
import com.neonlab.product.enums.Units;
import com.neonlab.product.enums.VarietyType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductVarietyResponse {

    private String name;
    private String code;
    private String category;
    private String subCategory;
    private String description;
    private String brand;
    private VarietyType type;
    private String value;
    private Units unit;
    private String varietyDescription;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer quantity;
    private List<String> documents;

    @Builder(builderMethodName = "buildByProductVarietyAndDocuments")
    public ProductVarietyResponse(
            final Product product,
            final Variety variety,
            final List<String> documents
            ){
        this.name = product.getName();
        this.code = product.getCode();
        this.category = product.getCategory();
        this.subCategory = product.getSubCategory();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.type = variety.getType();
        this.value = variety.getValue();
        this.unit = variety.getUnit();
        this.varietyDescription = variety.getDescription();
        this.price = variety.getPrice();
        this.discountedPrice = MathUtils.getDiscountedPrice(this.price, variety.getDiscountPercent());
        this.quantity = variety.getQuantity();
        this.documents = documents;
    }


}
