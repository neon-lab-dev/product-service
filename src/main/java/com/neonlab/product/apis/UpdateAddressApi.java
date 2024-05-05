package com.neonlab.product.apis;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UpdateAddressApi {
    @Autowired
    AddressService addressService;
    @Autowired
    ValidationUtils validationUtils;

    public ApiOutput<?> update(AddressDto addressDto) {
        try {
            validationUtils.validate(addressDto, AddOrderValidationGroup.class);
            var address = addressService.update(addressDto);
            return new ApiOutput<>(HttpStatus.OK.value(),"Address Updated Successfully" ,address);
        }catch (InvalidInputException | ServerException | ConstraintViolationException e ){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
