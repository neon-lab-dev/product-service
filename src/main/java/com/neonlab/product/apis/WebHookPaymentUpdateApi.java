package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.models.PaymentResponse;
import com.neonlab.common.models.razorpay.WebhookPaymentModel;
import com.neonlab.common.services.PaymentRecordService;
import com.neonlab.common.utilities.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ApiOutput<PaymentResponse> process(WebhookPaymentModel request) throws InvalidInputException {
        validate(request);
        return null;
    }

    private void validate(WebhookPaymentModel request) throws InvalidInputException {
        try {
            var compiledSignature = EncryptionUtils.getHmacSignature("kaserapay", request.getBody(), "HmacSHA256");
            if (!Objects.equals(compiledSignature, request.getSignature())){
                throw new InvalidInputException("Unauthorized access.");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

}
