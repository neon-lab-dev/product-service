package com.neonlab.product.models.searchCriteria;

import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

import java.math.BigDecimal;

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
    private String code;

    /**
     * filter by product minimum price
     */
    private BigDecimal minimumPrice;

    /**
     * filter by product maximum price
     */
    private BigDecimal maximumPrice;

    @Builder(builderMethodName = "productSearchCriteriaBuilder")
    public ProductSearchCriteria(
            final String name,
            final String category,
            final String subCategory,
            final String brand,
            final String code,
            final BigDecimal minimumPrice,
            final BigDecimal maximumPrice,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Direction sortDirection
            ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.code = code;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
    }



}
