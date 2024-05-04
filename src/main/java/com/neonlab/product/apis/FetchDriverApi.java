package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.models.searchCriteria.DriverSearchCriteria;
import com.neonlab.product.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.netty.http.client.Http2AllocationStrategy;

@Slf4j
@Loggable
@Service
public class FetchDriverApi {
    @Autowired
    private DriverService  driverService;

    public ApiOutput<?> fetchDriver(DriverSearchCriteria searchCriteria){
        return new ApiOutput<>(HttpStatus.OK.value(),null ,driverService.fetchDriver(searchCriteria));
    }
}
