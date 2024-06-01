package com.neonlab.product.controller;

import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.models.PaymentRequest;
import com.neonlab.common.models.razorpay.WebhookPaymentModel;
import com.neonlab.product.apis.FetchPaymentApi;
import com.neonlab.product.apis.WebHookPaymentUpdateApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final FetchPaymentApi fetchPaymentApi;
    private final WebHookPaymentUpdateApi webHookPaymentUpdateApi;

    @GetMapping("/get")
    public ApiOutput<?> getPayment(final PaymentRequest request){
        return fetchPaymentApi.process(request);
    }

    @PostMapping("/webhook/update")
    public ApiOutput<?> webhookPaymentUpdate(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestHeader("x-razorpay-event-id") String eventId,
            @RequestBody String body
            ) throws InvalidInputException {
        log.info(String.format("Signature %s | eventId %s | body %s", signature, eventId, body));
        webHookPaymentUpdateApi.process(new WebhookPaymentModel(signature, eventId, body));
        return new ApiOutput<>(HttpStatus.OK.value(), null, "Ok");
    }

}
