package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.PageableResponse;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.product.dtos.DriverDto;
import com.neonlab.product.entities.Driver;
import com.neonlab.product.models.searchCriteria.DriverSearchCriteria;
import com.neonlab.product.repository.DriverRepository;
import com.neonlab.product.repository.specifications.DriverSpecifications;
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

    public final static String DELETE_MESSAGE = "Driver has been deleted.";

    @Autowired
    private UserService userService;

    @Autowired
    private DriverRepository driverRepository;

    public DriverDto add(DriverDto driverDto) throws ServerException, InvalidInputException {
        Driver newDriver=new Driver(driverDto);
        var loggedInUser = userService.getLoggedInUser();
        newDriver.setCreatedBy(loggedInUser.getPrimaryPhoneNo());
        Driver driver=driverRepository.save(newDriver);
        return ObjectMapperUtils.map(driver, DriverDto.class);
    }

    public String delete(List<String> ids) {
        driverRepository.deleteAllById(ids);
        return DELETE_MESSAGE;
    }

    public DriverDto update(DriverDto driverDto) throws ServerException, InvalidInputException {
        var driver = fetchById(driverDto.getId());
        ObjectMapperUtils.map(driverDto, driver);
        driver = driverRepository.save(driver);
        return ObjectMapperUtils.map(driver, DriverDto.class);
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

    public Driver fetchById(String id) throws InvalidInputException {
        return driverRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Driver not found with id "+id));
    }

    public Optional<Driver> getByContactNo(String contactNo){
        return driverRepository.findByContactNo(contactNo);
    }

    public Optional<Driver> getByVehicleNo(String vehicleNo){
        return driverRepository.findByVehicleNo(vehicleNo);
    }

}
