package com.neonlab.product.models.searchCriteria;

import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import com.neonlab.product.enums.VarietyType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductSearchCriteria extends PageableSearchCriteria {

    /**
     * filter by product name
     */
    private String name;

    /**
     * filter by product category
     */
    private String category;

    /**
     * filter by product sub-category
     */
    private String subCategory;

    /**
     * filter by product brand
     */
    private String brand;

    /**
     * filter by product code
     */
    private List<String> codes;

    /**
     * filter by varietyType
     */
    private VarietyType varietyType;

    /**
     * filter by varietyValue
     */
    private String value;

    /**
     * filter by varietyValue
     */
    private String varietyDescription;

    /**
     * filter by product minimum price
     */
    private BigDecimal minimumPrice;

    /**
     * filter by product maximum price
     */
    private BigDecimal maximumPrice;
    /**
     * filter by product maximum price
     */
    private Integer quantity;

    @Builder(builderMethodName = "productSearchCriteriaBuilder")
    public ProductSearchCriteria(
            final String name,
            final String category,
            final String subCategory,
            final String brand,
            final List<String> codes,
            final VarietyType varietyType,
            final String value,
            final BigDecimal minimumPrice,
            final BigDecimal maximumPrice,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Direction sortDirection,
            final Integer quantity
            ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.codes = codes;
        this.varietyType = varietyType;
        this.value = value;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.quantity = quantity;
    }



}
