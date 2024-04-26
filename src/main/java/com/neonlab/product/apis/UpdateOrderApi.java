package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class UpdateOrderApi {
    @Autowired
    OrderService orderService;

    public ApiOutput<?> updateOrder(String orderId, String orderStatus) {
        try{
            validate(orderId,orderStatus);
            return orderService.updateOrder(orderId,orderStatus);
        }catch (InvalidInputException | ServerException e ){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(String orderId,String orderStatus) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(orderId)){
            throw new InvalidInputException("OrderId is Mandatory");
        }

        if(StringUtil.isNullOrEmpty(orderStatus)){
            throw new InvalidInputException("Please add something to Update Order");
        }
    }
}
