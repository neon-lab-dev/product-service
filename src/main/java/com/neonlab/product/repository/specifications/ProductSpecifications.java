package com.neonlab.product.repository.specifications;

import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Objects;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.ProductEntityConstant.*;

public class ProductSpecifications {

    public static Specification<Product> buildSearchCriteria(final ProductSearchCriteria searchCriteria){
        var retVal = Specification.<Product>where(null);
        if (!StringUtil.isNullOrEmpty(searchCriteria.getName())){
            retVal = retVal.and(filterByNameLike(searchCriteria.getName()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getCategory())){
            retVal = retVal.and(filterByCategory(searchCriteria.getCategory()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getSubCategory())){
            retVal = retVal.and(filterBySubCategory(searchCriteria.getSubCategory()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getBrand())){
            retVal = retVal.and(filterByBrand(searchCriteria.getBrand()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getCode())){
            retVal = retVal.and(filterByCode(searchCriteria.getCode()));
        }
        if (Objects.nonNull(searchCriteria.getMinimumPrice())){
            retVal = retVal.and(filterByMinimumPrice(searchCriteria.getMinimumPrice()));
        }
        if (Objects.nonNull(searchCriteria.getMaximumPrice())){
            retVal = retVal.and(filterByMaximumPrice(searchCriteria.getMaximumPrice()));
        }
        return retVal;
    }

    private static Specification<Product> filterByNameLike(final String name){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(NAME), withLikePattern(name))
                );
    }

    private static Specification<Product> filterByCategory(final String category){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(CATEGORY), withLikePattern(category))
                );
    }

    private static Specification<Product> filterBySubCategory(final String subCategory){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(SUB_CATEGORY), withLikePattern(subCategory))
                );
    }

    private static Specification<Product> filterByBrand(final String brand){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(BRAND), withLikePattern(brand))
                );
    }

    private static Specification<Product> filterByCode(final String code){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(CODE), code)
                );
    }

    private static Specification<Product> filterByMinimumPrice(final BigDecimal startingPrice){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), startingPrice)
                );
    }

    private static Specification<Product> filterByMaximumPrice(final BigDecimal endingPrice){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), endingPrice)
                );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }

    private static String withLikePatternStartWith(String str){
        return str + PERCENTAGE;
    }

}
