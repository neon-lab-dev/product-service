package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.models.responses.ProductReportModel;
import com.neonlab.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
@RequiredArgsConstructor
public class ProductReportApi {

    private final ProductService productService;

    public ApiOutput<ProductReportModel> process(){
        return new ApiOutput<>(HttpStatus.OK.value(), null, productService.getReport());
    }

}
