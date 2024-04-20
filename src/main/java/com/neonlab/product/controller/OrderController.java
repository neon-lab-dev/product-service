/*
package com.neonlab.product.controller;


import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.CreateOrderApi;
import com.neonlab.product.dtos.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("v1/order")
@RequiredArgsConstructor
public class OrderController {

    CreateOrderApi createOrderApi;

    @PostMapping("/create")
    public ApiOutput<?> createOrder(@RequestBody OrderDto orderDto) {
        return createOrderApi.createOrder(orderDto);
    }
}

 */
