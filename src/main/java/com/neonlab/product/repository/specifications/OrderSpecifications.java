package com.neonlab.product.repository.specifications;

import com.neonlab.common.entities.Address;
import com.neonlab.common.entities.Order;
import com.neonlab.common.enums.OrderStatus;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.constants.EntityConstant;
import com.neonlab.product.models.searchCriteria.OrderSearchCriteria;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Objects;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.EntityConstant.Address.CITY;
import static com.neonlab.product.constants.EntityConstant.Address.STATE;
import static com.neonlab.product.constants.EntityConstant.Order.*;

public class OrderSpecifications {

    public static Specification<Order> buildSearchCriteria(final OrderSearchCriteria searchCriteria){
        var retVal = Specification.<Order>where(null);
        if (!searchCriteria.isAdmin()){
            retVal = retVal.and(filterByUserId(searchCriteria.getUserId()));
        }
        if (!CollectionUtils.isEmpty(searchCriteria.getVarietyIds())){
            Specification<Order> varietySpecification = Specification.where(null);
            for (var id : searchCriteria.getVarietyIds()){
                varietySpecification = varietySpecification.and(filterByVarietyIdLike(id));
            }
            retVal = retVal.and(varietySpecification);
        }
        if (Objects.nonNull(searchCriteria.getMinPrice())){
            retVal = retVal.and(filterByMinimumPrice(searchCriteria.getMinPrice()));
        }
        if (Objects.nonNull(searchCriteria.getMaxPrice())){
            retVal = retVal.and(filterByMaximumPrice(searchCriteria.getMaxPrice()));
        }
        if (Objects.nonNull(searchCriteria.getOrderStatus())){
            retVal = retVal.and(filterByOrderStatus(searchCriteria.getOrderStatus()));
        }
        return retVal;
    }

    private static Specification<Order> filterByUserId(final String userId){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(USER_ID), userId));
    }

    private static Specification<Order> filterByVarietyIdLike(final String id){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(BOUGHT_PRODUCT_DETAILS), withLikePattern(id)));
    }

    private static Specification<Order> filterByMinimumPrice(final BigDecimal minPrice){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(TOTAL_COST), minPrice)
        );
    }

    private static Specification<Order> filterByMaximumPrice(final BigDecimal maxPrice){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(TOTAL_COST), maxPrice)
        );
    }

    private static Specification<Order> filterByOrderStatus(final OrderStatus orderStatus){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ORDER_STATUS), orderStatus));
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }

}
