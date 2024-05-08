package com.neonlab.product.models.searchCriteria;

import com.neonlab.common.enums.OrderStatus;
import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderSearchCriteria extends PageableSearchCriteria {

    /**
     * filter by orderId
     */
    private String orderId;
    /**
     * filter by varietyId
     */
    private List<String> varietyIds;
    /**
     * filter by orderStatus
     */
    private OrderStatus orderStatus;
    /**
     * filter by minPrice
     */
    private BigDecimal minPrice;
    /**
     * filter by maxPrice
     */
    private BigDecimal maxPrice;
    /**
     * filter by primaryPhoneNo
     */
    private String primaryPhoneNo;
    /**
     * filter by userId
     */
    private String userId;

    /**
     * filter by admin
     */
    private boolean admin;

    @Builder(builderMethodName = "orderSearchCriteriaBuilder")
    public OrderSearchCriteria(
            final String orderId,
            final List<String> varietyIds,
            final OrderStatus orderStatus,
            final BigDecimal minPrice,
            final BigDecimal maxPrice,
            final String userId,
            final Boolean admin,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Sort.Direction sortDirection
    ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.orderId = orderId;
        this.varietyIds = varietyIds;
        this.orderStatus = orderStatus;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.userId = userId;
        this.admin = admin;
    }


}
