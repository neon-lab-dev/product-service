package com.neonlab.product.apis;

import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.models.PaymentRequest;
import com.neonlab.common.models.PaymentResponse;
import com.neonlab.common.services.PaymentServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Loggable
public class FetchPaymentLinkApi {

    @Autowired private PaymentServiceProvider serviceProvider;

    public ApiOutput<PaymentResponse> process(PaymentRequest request){
        try {
            var service = serviceProvider.getService(request.getOnline());
            var retVal = service.createPayment(request);
            return new ApiOutput<>(HttpStatus.OK.value(), null, retVal);
        } catch (InvalidInputException | ServerException | JsonParseException e) {
            log.error("Error: {}",e.getMessage());
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
