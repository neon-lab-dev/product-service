package com.neonlab.product.apis;
import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.common.validationGroups.AddOrderValidationGroup;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.service.OrderService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class CreateOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<?> create(OrderDto orderDto) {
        try {
            validate(orderDto);
            return new ApiOutput<>(HttpStatus.OK.value(), null, orderService.createOrder(orderDto));
        } catch (ConstraintViolationException | InvalidInputException | ServerException | JsonParseException e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(OrderDto orderDto) throws InvalidInputException {
        validationUtils.validate(orderDto);
        if(orderService.paymentIdExist(orderDto.getPaymentId())){
            throw new InvalidInputException("Order with same payment id exist.");
        }
        try {
            validationUtils.validate(orderDto.getShippingInfo(), AddOrderValidationGroup.class);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Shipping details is mandatory.");
        }

        orderService.createOrderValidations(orderDto);
    }

}