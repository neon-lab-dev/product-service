package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Driver;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.responses.PageableResponse;
import com.neonlab.product.models.searchCriteria.DriverSearchCriteria;
import com.neonlab.product.repository.DriverRepository;
import com.neonlab.product.repository.specifications.DriverSpecifications;
import com.neonlab.product.repository.specifications.ProductSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
            driver.setName(driverDto.getName());
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

    public PageableResponse<DriverDto> fetchDriver(final DriverSearchCriteria searchCriteria){
        var pageable= PageableUtils.createPageable(searchCriteria);
        Page<Driver> drivers = driverRepository.findAll(
                DriverSpecifications.buildSearchCriteria(searchCriteria),
                pageable
        );
        var reslutList = drivers.getContent().stream()
                .map(DriverService::getDriverDto)
                .filter(Objects::nonNull)
                .toList();
        return new PageableResponse<>(reslutList, searchCriteria);

    }
    private static DriverDto getDriverDto(Driver driver) {
        DriverDto retVal = null;
        try{
            retVal = ObjectMapperUtils.map(driver, DriverDto.class);
        } catch (ServerException ignored){}
        return retVal;
    }
}
