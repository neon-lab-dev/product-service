package com.neonlab.product.repository.specifications;

import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.EntityConstant.Product.*;
import static com.neonlab.product.constants.EntityConstant.Variety.*;

public class ProductSpecifications {

    public static Specification<Product> buildSearchCriteria(final ProductSearchCriteria searchCriteria){
        var retVal = Specification.<Product>where(null);
        if (!StringUtil.isNullOrEmpty(searchCriteria.getName())){
            retVal = retVal.and(filterByNameLike(searchCriteria.getName()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getCategory())){
            retVal = retVal.and(filterByCategoryLike(searchCriteria.getCategory()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getSubCategory())){
            retVal = retVal.and(filterBySubCategoryLike(searchCriteria.getSubCategory()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getSubCategory2())){
            retVal = retVal.and(filterBySubCategory2Like(searchCriteria.getSubCategory2()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getBrand())){
            retVal = retVal.and(filterByBrandLike(searchCriteria.getBrand()));
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getCodes())){
            retVal = retVal.and(filterByCodeIn(searchCriteria.getCodes()));
        }
        if (Objects.nonNull(searchCriteria.getMinimumPrice())){
            retVal = retVal.and(filterByMinimumPrice(searchCriteria.getMinimumPrice()));
        }
        if (Objects.nonNull(searchCriteria.getMaximumPrice())){
            retVal = retVal.and(filterByMaximumPrice(searchCriteria.getMaximumPrice()));
        }
        if (Objects.nonNull(searchCriteria.getQuantity())){
            retVal = retVal.and(filterByVarietyQuantity(searchCriteria.getQuantity()));
        }
        if (!StringUtil.isNullOrEmpty(searchCriteria.getVarietyDescription())){
            var words = searchCriteria.getVarietyDescription().split(" ");
            Specification<Product> varietySpecification = Specification.where(null);
            for (var word : words){
                varietySpecification = varietySpecification.and(filterByVarietyDescriptionLike(withLikePattern(word)));
            }
            retVal = retVal.and(varietySpecification);
        }
        return retVal;
    }

    private static Specification<Product> filterByNameLike(final String name){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), withLikePattern(name)));
    }

    private static Specification<Product> filterByCategoryLike(final String category){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(CATEGORY), withLikePattern(category))
        );
    }

    private static Specification<Product> filterBySubCategoryLike(final String subCategory){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(SUB_CATEGORY), withLikePattern(subCategory))
        );
    }

    private static Specification<Product> filterBySubCategory2Like(final String subCategory2){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(SUB_CATEGORY2), withLikePattern(subCategory2))
        );
    }

    private static Specification<Product> filterByBrandLike(final String brand){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(BRAND), withLikePattern(brand))
        );
    }

    private static Specification<Product> filterByCodeIn(final List<String> codes){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(CODE)).value(codes)
        );
    }

    private static Specification<Product> filterByMinimumPrice(final BigDecimal startingPrice){
        return ((root, query, criteriaBuilder) ->
        {
            var productVarietyJoin = root.join(VARIETIES);
            return criteriaBuilder.greaterThanOrEqualTo(productVarietyJoin.get(PRICE), startingPrice);
        }
        );
    }

    private static Specification<Product> filterByMaximumPrice(final BigDecimal endingPrice){
        return ((root, query, criteriaBuilder) ->
        {
            var productVarietyJoin = root.join(VARIETIES);
            return criteriaBuilder.lessThanOrEqualTo(productVarietyJoin.get(PRICE), endingPrice);
        }
        );
    }

    private static Specification<Product> filterByVarietyDescriptionLike(final String varietyDescription){
        return ((root, query, criteriaBuilder) ->
        {
            var productVarietyJoin = root.join(VARIETIES);
            return criteriaBuilder.like(productVarietyJoin.get(DESCRIPTION), varietyDescription);
        }
        );
    }

    private static Specification<Product> filterByVarietyQuantity(final Integer quantity){
        return ((root, query, criteriaBuilder) ->
        {
            var productVarietyJoin = root.join(VARIETIES);
            return criteriaBuilder.equal(productVarietyJoin.get(QUANTITY), quantity);
        }
        );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }

}
