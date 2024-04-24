package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Loggable
public class DeleteDriverApi {
    @Autowired
    private DriverService driverService;

    public ApiOutput<Void> deleteDriver(List<String> ids){

        try {
            validate(ids);
            String msg= driverService.deleteDriver(ids);
                return new ApiOutput<>(HttpStatus.OK.value(), msg);
        }catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    private void validate(List<String> ids) throws InvalidInputException{

        if(ids.isEmpty()){
            throw new InvalidInputException("Driver id is not provided.");
        }
    }
}
