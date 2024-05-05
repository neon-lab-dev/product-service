package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.models.requests.UpdateOrderRequest;
import com.neonlab.product.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
@Loggable
@RequiredArgsConstructor
public class CancelOrderApi {

    private final OrderService orderService;
    private final UpdateOrderApi updateOrderApi;

    public ApiOutput<OrderDto> process(String orderId) {
        try {
            validate(orderId);
            var updateOrderRequest = UpdateOrderRequest.getCancelOrderRequest(orderId);
            return new ApiOutput<>(HttpStatus.OK.value(), null, updateOrderApi.process(updateOrderRequest).getResponseBody());
        }catch (InvalidInputException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(String orderId) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(orderId)){
            throw new InvalidInputException("Order Id is Mandatory");
        }
        if(!orderService.canOrderCancel(orderId)){
            throw new InvalidInputException("Order Can not be cancel");
        }
    }
}
