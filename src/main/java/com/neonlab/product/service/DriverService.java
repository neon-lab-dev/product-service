package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.entities.Driver;
import com.neonlab.product.repository.DriverRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Loggable
@Slf4j
public class DriverService {

    public final static String DELETE_MESSAGE = "Drivers Has been deleted.";

    @Autowired
    private DriverRepository driverRepository;

    public DriverDto addDriver(DriverDto driverDto) throws ServerException {
        Driver newDriver=new Driver(driverDto);
        Driver driver=driverRepository.save(newDriver);
        DriverDto response=new DriverDto(driver.getId(), driver.getName(), driver.getContactNo(), driver.getVehicleNo(), driver.isAvailable());
        return response;
    }
    public String deleteDriver(List<String> ids) throws ServerException{
        driverRepository.deleteAllById(ids);
        return DELETE_MESSAGE;
    }

    public DriverDto updateDriver(DriverDto driverDto) throws ServerException, InvalidInputException {
        Optional<Driver> optionalDriver=driverRepository.findById(driverDto.getId());
        if(optionalDriver.isEmpty()){
            throw new InvalidInputException("invalid driver id");
        }
        Driver driver=optionalDriver.get();
        if(driverDto.getName()!=null){
            driver.setName(driver.getName());
        }
        if(driverDto.getContactNo()!=null){
            driver.setContactNo(driverDto.getContactNo());
        }
        if(driverDto.getVehicleNo()!=null){
            driver.setVehicleNo(driverDto.getVehicleNo());
        }
        Driver updatedDriver=driverRepository.save(driver);
        DriverDto response=new DriverDto(updatedDriver.getId(),updatedDriver.getName(),updatedDriver.getContactNo(),updatedDriver.getVehicleNo(), updatedDriver.isAvailable());
        return response;
    }
}
