package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.OrderReportApi;
import com.neonlab.product.apis.ProductReportApi;
import com.neonlab.product.models.responses.OrderReportModel;
import com.neonlab.product.models.responses.ProductReportModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Loggable
@RequestMapping("/v1/report")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ProductReportApi productReportApi;
    private final OrderReportApi orderReportApi;


    @GetMapping(value = "/product")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<ProductReportModel> getProductReport(){
        return productReportApi.process();
    }

    @GetMapping(value = "/order")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<OrderReportModel> getOrderReport(){
        return orderReportApi.process();
    }

}
