package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class AddDriverApi {
    @Autowired
    private DriverService driverService;
    public ApiOutput<DriverDto> addDriver(DriverDto driverDto){
        try{
            validate(driverDto);
            DriverDto response=driverService.addDriver(driverDto);
            return new ApiOutput<>(HttpStatus.OK.value(),"Driver added successfully",response);
        }
        catch (Exception e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
    }
    private void validate(DriverDto driverDto) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(driverDto.getName())){
            throw new InvalidInputException("Driver name is not provided");
        }
        if(StringUtil.isNullOrEmpty(driverDto.getContactNo())){
            throw new InvalidInputException("Contact number is not provided");
        }
        if(StringUtil.isNullOrEmpty(driverDto.getVehicleNo())){
            throw new InvalidInputException("Vehicle number is not provided");
        }

    }
}
