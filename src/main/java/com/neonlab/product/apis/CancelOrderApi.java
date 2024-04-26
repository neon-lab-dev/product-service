package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class CancelOrderApi {
    @Autowired
    OrderService orderService;

    public ApiOutput<?> cancelOrder(String orderId) {
        try {
            validate(orderId);
            return orderService.cancelOrder(orderId);
        }catch (InvalidInputException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(String orderId) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(orderId)){
            throw new InvalidInputException("Order Id is Mandatory");
        }
    }
}
