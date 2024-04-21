package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.dtos.AddDriverRequest;
import com.neonlab.product.dtos.AddDriverResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Loggable
public class DriverController {
    @PostMapping("/addDriver")
    public ApiOutput<AddDriverResponse> addDriver(@RequestBody AddDriverRequest addDriverRequest){
    }
}
