package com.neonlab.product.controller;

import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.models.PaymentRequest;
import com.neonlab.product.apis.FetchPaymentLinkApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final FetchPaymentLinkApi fetchPaymentLinkApi;

    @GetMapping("get/payment-link")
    public ApiOutput<?> getPaymentLink(final PaymentRequest request){
        return fetchPaymentLinkApi.process(request);
    }
}
