package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.dtos.AddDriverRequest;
import com.neonlab.product.dtos.AddDriverResponse;
import com.neonlab.product.entities.Driver;
import com.neonlab.product.repository.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Loggable
@Slf4j
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    public AddDriverResponse addDriver(AddDriverRequest addDriverRequest) throws ServerException {
        Driver newDriver=new Driver(addDriverRequest.getName(), addDriverRequest.getContactNo(), addDriverRequest.getVehicleNo());
        Driver driver=driverRepository.save(newDriver);
        AddDriverResponse response=new AddDriverResponse(driver.getId(), driver.getName(), driver.getContactNo(), driver.getVehicleNo(), driver.isAvailable());
        return response;
    }
}
