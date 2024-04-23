package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.AddDriverApi;
import com.neonlab.product.apis.DeleteDriverApi;
import com.neonlab.product.apis.UpdateDriverApi;
import com.neonlab.product.dtos.DriverDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiOutput<DriverDto> addDriver(@RequestBody DriverDto driverDto){
        return addDriverApi.addDriver(driverDto);
    }

    @DeleteMapping("/delete")
    public ApiOutput<Void> deleteDriver(@RequestParam String id){
        return deleteDriverApi.deleteDriver(id);
    }

    @PostMapping("/update")
    public ApiOutput<DriverDto> updateDriver(@RequestBody DriverDto driverDto){
        return updateDriverApi.updateDriver(driverDto);
    }
}
