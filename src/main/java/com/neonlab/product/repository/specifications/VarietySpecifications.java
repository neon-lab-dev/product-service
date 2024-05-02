package com.neonlab.product.repository.specifications;

import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.entities.Product;
import com.neonlab.product.entities.Variety;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Objects;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.EntityConstant.Product.*;
import static com.neonlab.product.constants.EntityConstant.Variety.*;

public class VarietySpecifications {

    public static Specification<Variety> buildSearchCriteria(final ProductSearchCriteria searchCriteria){
        var retVal = Specification.<Variety>where(null);
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
        if (!StringUtil.isNullOrEmpty(searchCriteria.getVarietyDescription())){
            var words = searchCriteria.getVarietyDescription().split(" ");
            Specification<Variety> varietySpecification = Specification.where(null);
            for (var word : words){
                varietySpecification = varietySpecification.and(filterByVarietyDescriptionLike(withLikePattern(word)));
            }
            retVal = retVal.and(varietySpecification);
        }
        return retVal;
    }

    private static Specification<Variety> filterByNameLike(final String name){
        return ((root, query, criteriaBuilder) ->
        {
            Join<Variety, Product> productVarietyJoin = root.join(PRODUCT);
            return criteriaBuilder.like(productVarietyJoin.get(NAME), withLikePattern(name));
        }
        );
    }

    private static Specification<Variety> filterByCategory(final String category){
        return ((root, query, criteriaBuilder) ->
        {
            Join<Variety, Product> productVarietyJoin = root.join(PRODUCT);
            return criteriaBuilder.like(productVarietyJoin.get(CATEGORY), withLikePattern(category));
        }
        );
    }

    private static Specification<Variety> filterBySubCategory(final String subCategory){
        return ((root, query, criteriaBuilder) ->
        {
            Join<Variety, Product> productVarietyJoin = root.join(PRODUCT);
            return criteriaBuilder.like(productVarietyJoin.get(SUB_CATEGORY), withLikePattern(subCategory));
        }
        );
    }

    private static Specification<Variety> filterByBrand(final String brand){
        return ((root, query, criteriaBuilder) ->
        {
            Join<Variety, Product> productVarietyJoin = root.join(PRODUCT);
            return criteriaBuilder.like(productVarietyJoin.get(BRAND), withLikePattern(brand));
        }
        );
    }

    private static Specification<Variety> filterByCode(final String code){
        return ((root, query, criteriaBuilder) ->
        {
            Join<Variety, Product> productVarietyJoin = root.join(PRODUCT);
            return criteriaBuilder.equal(productVarietyJoin.get(CODE), code);
        }
        );
    }

    private static Specification<Variety> filterByMinimumPrice(final BigDecimal startingPrice){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), startingPrice)
                );
    }

    private static Specification<Variety> filterByMaximumPrice(final BigDecimal endingPrice){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), endingPrice)
                );
    }

    private static Specification<Variety> filterByVarietyDescriptionLike(final String varietyDescription){
        return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(DESCRIPTION), varietyDescription)
                );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }

}
