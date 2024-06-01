package com.neonlab.product.apis;

import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.models.PaymentResponse;
import com.neonlab.common.models.PaymentStatusRequest;
import com.neonlab.common.services.PaymentRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Loggable
@RequiredArgsConstructor
public class PaymentStatusApi {

    private final PaymentRecordService paymentRecordService;

    public ApiOutput<PaymentResponse> process(PaymentStatusRequest request) throws InvalidInputException, ServerException, JsonParseException {
        var retVal = paymentRecordService.fetchStatus(request);
        return new ApiOutput<>(HttpStatus.OK.value(), null, retVal);
    }

}
