package com.neonlab.product.apis;

import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.models.PaymentResponse;
import com.neonlab.common.models.razorpay.WebhookPaymentModel;
import com.neonlab.common.services.PaymentRecordService;
import com.neonlab.common.utilities.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@Service
@Loggable
@RequiredArgsConstructor
public class WebHookPaymentUpdateApi {

    private final PaymentRecordService paymentRecordService;

    public ApiOutput<String> process(WebhookPaymentModel request) throws InvalidInputException {
        validate(request);
        try {
            return new ApiOutput<>(HttpStatus.OK.value(), null, paymentRecordService.processWebhook(request));
        } catch (JsonParseException | ServerException e) {
            log.error(e.getMessage());
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(WebhookPaymentModel request) throws InvalidInputException {
        try {
            paymentRecordService.validateWebhook(request);
        } catch (NoSuchAlgorithmException | InvalidKeyException | JsonParseException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

}
