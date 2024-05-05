package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.common.validationGroups.UpdateValidationGroup;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Loggable
public class UpdateDriverApi {

    public static final String UPDATE_MESSAGE="Driver information is updated successfully";

    @Autowired
    private DriverService driverService;

    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<DriverDto> process(DriverDto driverDto){
        try{
            validate(driverDto);
            DriverDto response=driverService.update(driverDto);
            return new ApiOutput<>(HttpStatus.OK.value(), UPDATE_MESSAGE,response);
        } catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    public void validate(DriverDto driverDto) throws InvalidInputException {
        if(Objects.isNull(driverDto)){
            throw new InvalidInputException("Please provide something to update");
        }
        validationUtils.validate(driverDto, UpdateValidationGroup.class);
        driverService.fetchById(driverDto.getId());
    }

}
