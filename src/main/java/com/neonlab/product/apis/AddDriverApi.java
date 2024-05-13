package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.entities.Driver;
import com.neonlab.product.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class AddDriverApi {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<DriverDto> addDriver(DriverDto driverDto){
        try{
            validate(driverDto);
            DriverDto response=driverService.add(driverDto);
            return new ApiOutput<>(HttpStatus.OK.value(),"Driver added successfully",response);
        }
        catch (Exception e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
    }

    private void validate(DriverDto driverDto) throws InvalidInputException {
        validationUtils.validate(driverDto);
        var driver = driverService.getByContactNo(driverDto.getContactNo());
        if (driver.isPresent()){
            throw new InvalidInputException("Driver exists with contact no "+ driverDto.getContactNo());
        }
        driver = driverService.getByVehicleNo(driverDto.getVehicleNo());
        if (driver.isPresent()){
            throw new InvalidInputException("Driver exists with vehicle no "+ driverDto.getVehicleNo());
        }
        //TODO: Add validation for valid phone no (10 characters)
        if(driverDto.getContactNo().length()!=10){
            throw new InvalidInputException("Please enter valid contact number.");
        }
    }
}
