package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.models.responses.OrderReportModel;
import com.neonlab.product.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
@RequiredArgsConstructor
public class OrderReportApi {

    private final OrderService orderService;

    public ApiOutput<OrderReportModel> process(){
        return new ApiOutput<>(HttpStatus.OK.value(), null, orderService.getReport());
    }

}
