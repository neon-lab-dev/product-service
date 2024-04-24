package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.service.DriverService;
import org.apache.logging.log4j.util.Strings;
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

    public ApiOutput<DriverDto> updateDriver(DriverDto driverDto){
        try{
            validate(driverDto);
            DriverDto response=driverService.updateDriver(driverDto);
            return new ApiOutput<>(HttpStatus.OK.value(), UPDATE_MESSAGE,response);
        } catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    public void validate(DriverDto driverDto) throws InvalidInputException {
        if(Objects.isNull(driverDto)){
            throw new InvalidInputException("Please provide something to update");
        }
        if(Strings.isEmpty(driverDto.getId())){
            throw new InvalidInputException("You need to provide id to update the driver");
        }
    }

}
