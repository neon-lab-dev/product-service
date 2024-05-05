package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;


@Service
public class DeleteAddressApi {

    @Autowired
    private AddressService addressService;
    @Autowired
    private ValidationUtils validationUtils;


    public ApiOutput<?> delete(List<String> ids) {
        try{
            validate(ids);
            String message = addressService.delete(ids);
            return new ApiOutput<>(HttpStatus.OK.value(), message);
        }catch (ConstraintViolationException | InvalidInputException e ){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(List<String> ids) throws InvalidInputException {
        if(!Objects.isNull(ids) && !addressService.validateIds(ids)){
            throw new InvalidInputException("Address Id Should not be Empty");
        }
    }
}
