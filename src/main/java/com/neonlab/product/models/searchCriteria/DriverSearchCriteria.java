package com.neonlab.product.models.searchCriteria;


import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DriverSearchCriteria extends PageableSearchCriteria {
    /**
     * filter by driver name
     */
    private String name;
    /**
     * filter by driver contactNo
     */
    private String contactNo;
    /**
     * filter by driver vehicleNo
     */
    private String vehicleNo;
    /**
     * filter by driver's availability
     */
    private Boolean available;

    @Builder(builderMethodName = "driverSearchCriteriaBuilder")
    public DriverSearchCriteria(
            final String name,
            final String contactNo,
            final String vehicleNo,
            final boolean available,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Direction sortDirection
            ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.name=name;
        this.contactNo=contactNo;
        this.vehicleNo=vehicleNo;
        this.available=available;
    }

}
