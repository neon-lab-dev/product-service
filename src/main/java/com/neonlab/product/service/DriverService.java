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

import java.util.Optional;

@Service
@Loggable
@Slf4j
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    public DriverDto addDriver(DriverDto driverDto) throws ServerException {
        Driver newDriver=new Driver(driverDto);
        Driver driver=driverRepository.save(newDriver);
        DriverDto response=new DriverDto(driver.getId(), driver.getName(), driver.getContactNo(), driver.getVehicleNo(), driver.isAvailable());
        return response;
    }
    public String deleteDriver(String id) throws ServerException{
        driverRepository.deleteById(id);
        return "Driver Has been deleted.";
    }

    public DriverDto updateDriver(DriverDto driverDto) throws ServerException, InvalidInputException {
        Optional<Driver> optionalDriver=driverRepository.findById(driverDto.getId());
        int flag=0;
        if(optionalDriver.isEmpty()){
            throw new InvalidInputException("invalid driver id");
        }
        Driver driver=optionalDriver.get();
        if(driverDto.getName()!=null){
            flag=1;
            driver.setName(driver.getName());
        }
        if(driverDto.getContactNo()!=null){
            flag=1;
            driver.setContactNo(driverDto.getContactNo());
        }
        if(driverDto.getVehicleNo()!=null){
            flag=1;
            driver.setVehicleNo(driverDto.getVehicleNo());
        }
        if(flag==0){
            throw new InvalidInputException("please provide something to update");
        }
        Driver updatedDriver=driverRepository.save(driver);
        DriverDto response=new DriverDto(driver.getId(),driver.getName(),driver.getContactNo(),driver.getVehicleNo(), driverDto.isAvailable());
        return response;
    }
}
