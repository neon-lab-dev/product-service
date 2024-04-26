package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CreateOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<?> createOrder(OrderDto orderDto) {

        Optional<String> errorMessage = validationUtils.validation(orderDto,"orderDto");
       if(errorMessage.isPresent()){
           return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), errorMessage.get());
       }
        try {
            return orderService.createOrder(orderDto);
        } catch (InvalidInputException | ServerException e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}