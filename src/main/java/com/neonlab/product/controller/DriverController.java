package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.AddDriverApi;
import com.neonlab.product.apis.DeleteDriverApi;
import com.neonlab.product.apis.UpdateDriverApi;
import com.neonlab.product.dtos.DriverDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Loggable
@RequestMapping("/v1/driver")
public class DriverController {

    @Autowired
    private AddDriverApi addDriverApi;

    @Autowired
    private DeleteDriverApi deleteDriverApi;

    @Autowired
    private UpdateDriverApi updateDriverApi;


    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<DriverDto> addDriver(@RequestBody DriverDto driverDto){
        return addDriverApi.addDriver(driverDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<Void> deleteDriver(@RequestParam List<String> ids){
        return deleteDriverApi.deleteDriver(ids);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<DriverDto> updateDriver(@RequestBody DriverDto driverDto){
        return updateDriverApi.updateDriver(driverDto);
    }
}
