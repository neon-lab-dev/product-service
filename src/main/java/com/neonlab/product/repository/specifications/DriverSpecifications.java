package com.neonlab.product.repository.specifications;

import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.entities.Driver;

import com.neonlab.product.models.searchCriteria.DriverSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.product.constants.DriverEntityConstant.*;
import static com.neonlab.product.constants.ProductEntityConstant.NAME;

public class DriverSpecifications {
    public static Specification<Driver> buildSearchCriteria(final DriverSearchCriteria searchCriteria){
        var retVal=Specification.<Driver>where(null);
        if(!StringUtil.isNullOrEmpty(searchCriteria.getName())){
            retVal=retVal.and(filterByNameLike(searchCriteria.getName()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getContactNo())){
            retVal=retVal.and(filterByContactNoLike(searchCriteria.getContactNo()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getVehicleNo())){
            retVal=retVal.and(filterByContactNoLike(searchCriteria.getVehicleNo()));
        }
        if(searchCriteria.getAvailable()){
            retVal=retVal.and(filterByAvailableLike()
            );
        }
//        if(!searchCriteria.getAvailable()){
//            retVal=retVal.and(filterByAvailableLike(searchCriteria.getAvailable())
//            );
//        }
        return retVal;
    }

    private static Specification<Driver> filterByNameLike(final String name){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), withLikePattern(name))
        );
    }
    private static Specification<Driver> filterByContactNoLike(final String contactNo){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CONTACTNO), withLikePattern(contactNo))
        );
    }
    private static Specification<Driver> filterByVehicleLike(final String vehicleNo){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(VEHICLENO), withLikePatternStartWith(vehicleNo))
        );
    }
    private static Specification<Driver> filterByAvailableLike(){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get(AVAILABLE))
        );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }
    private static String withLikePatternStartWith(String str){
        return str + PERCENTAGE;
    }
}
