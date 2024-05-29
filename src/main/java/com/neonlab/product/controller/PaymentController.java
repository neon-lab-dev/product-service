package com.neonlab.product.controller;

import com.neonlab.common.dto.ApiOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @GetMapping("get/payment-link")
    public ApiOutput<?> getPaymentLink(
            @RequestParam(required = false, defaultValue = "false") Boolean cashOnDelivery,
            @RequestParam(required = false, defaultValue = "false") Boolean upiLink
    ){
        return null;
    }
}
