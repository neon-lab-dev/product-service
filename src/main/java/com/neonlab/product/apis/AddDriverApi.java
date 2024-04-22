package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.AddDriverRequest;
import com.neonlab.product.dtos.AddDriverResponse;
import com.neonlab.product.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Loggable
@Service
public class AddDriverApi {
    @Autowired
    private DriverService driverService;
    public ApiOutput<AddDriverResponse> addDriver(AddDriverRequest addDriverRequest){
        try{
            validate(addDriverRequest);
            AddDriverResponse response=driverService.addDriver(addDriverRequest);
            return new ApiOutput<>(HttpStatus.OK.value(),"Driver added successfully",response);
        }
        catch (Exception e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
    }
    private void validate(AddDriverRequest addDriverRequest) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(addDriverRequest.getName())){
            throw new InvalidInputException("Driver name is not provided");
        }
        if(StringUtil.isNullOrEmpty(addDriverRequest.getContactNo())){
            throw new InvalidInputException("Contact number is not provided");
        }
        if(StringUtil.isNullOrEmpty(addDriverRequest.getVehicleNo())){
            throw new InvalidInputException("Vehicle number is not provided");
        }

    }
}
