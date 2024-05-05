package com.neonlab.product.repository.specifications;

import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.entities.Driver;

import com.neonlab.product.models.searchCriteria.DriverSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.DriverEntityConstant.*;

public class DriverSpecifications {
    public static Specification<Driver> buildSearchCriteria(final DriverSearchCriteria searchCriteria){
        var retVal=Specification.<Driver>where(null);
        if(!StringUtil.isNullOrEmpty(searchCriteria.getName())){
            retVal=retVal.and(filterByNameLike(searchCriteria.getName()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getContactNo())){
            retVal=retVal.and(filterByContactNo(searchCriteria.getContactNo()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getVehicleNo())){
            retVal=retVal.and(filterByVehicleLike(searchCriteria.getVehicleNo()));
        }
        if(Objects.nonNull(searchCriteria.getAvailable())){
            retVal=retVal.and(filterByAvailableLike(searchCriteria.getAvailable()));
        }
        return retVal;
    }

    private static Specification<Driver> filterByNameLike(final String name){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), withLikePattern(name))
        );
    }
    private static Specification<Driver> filterByContactNo(final String contactNo){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CONTACT), contactNo)
        );
    }
    private static Specification<Driver> filterByVehicleLike(final String vehicleNo){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(VEHICLE), withLikePattern(vehicleNo))
        );
    }
    private static Specification<Driver> filterByAvailableLike(final boolean available){
        return ((root, query, criteriaBuilder) ->
        {
            if (available){
                return criteriaBuilder.isTrue(root.get(AVAILABLE));
            } else {
                return criteriaBuilder.isFalse(root.get(AVAILABLE));
            }
        }
        );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }
}
