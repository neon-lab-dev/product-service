//package com.neonlab.product.apis;
//
//import com.neonlab.common.annotations.Loggable;
//import com.neonlab.common.dto.ApiOutput;
//import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
//import com.neonlab.product.service.ProductService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Loggable
//@Service
//public class FetchProductApi {
//
//    @Autowired private ProductService service;
//
//    public ApiOutput<?> process(ProductSearchCriteria searchCriteria){
//            return new ApiOutput<>(HttpStatus.OK.value(), null, service.fetchProducts(searchCriteria));
//    }
//
//}
