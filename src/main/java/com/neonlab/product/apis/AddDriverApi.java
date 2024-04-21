package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.dtos.AddDriverRequest;
import com.neonlab.product.dtos.AddDriverResponse;
import org.springframework.stereotype.Service;

@Loggable
@Service
public class AddDriverApi {
    public ApiOutput<AddDriverResponse> addDriver(AddDriverRequest addDriverRequest){
    }
}
