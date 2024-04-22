package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;


@Service
public class CreateOrderApi {

    @Autowired
    private OrderService orderService;


    public ApiOutput<?> createOrder(@Valid OrderDto orderDto) {
        try {
            Validate(orderDto);
            return orderService.createOrder(orderDto);

        } catch (InvalidInputException | ServerException e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public void Validate(OrderDto orderDto) throws InvalidInputException {
        if (StringUtil.isNullOrEmpty(orderDto.getPaymentId())) {
            throw new InvalidInputException("Payment id is Mandatory");
        }
        if (StringUtil.isNullOrEmpty(orderDto.getAddressId())) {
            throw new InvalidInputException("Address Id is Mandatory");
        }
        if (Objects.isNull(orderDto.getProductCodeAndQuantity()) || orderDto.getProductCodeAndQuantity().isEmpty()) {
            throw new InvalidInputException("At-least add one Product code");
        }

        for (Map.Entry<String, Integer> entry : orderDto.getProductCodeAndQuantity().entrySet()) {
            if (entry.getValue() < 1) {
                throw new InvalidInputException("Product quantity must be 1 or more");
            }
        }
    }
}