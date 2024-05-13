package com.neonlab.product.apis;
import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.models.requests.UpdateOrderRequest;
import com.neonlab.product.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
@Loggable
@RequiredArgsConstructor
public class UpdateOrderApi {

    private final OrderService orderService;
    private final ValidationUtils validationUtils;

    public ApiOutput<OrderDto> process(UpdateOrderRequest request) {
        try{
            validate(request);
            return new ApiOutput<>(HttpStatus.OK.value(), null, orderService.update(request));
        }catch (InvalidInputException | ServerException | JsonParseException e ){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(UpdateOrderRequest request) throws InvalidInputException {
        validationUtils.validate(request);
        orderService.validateUpdateRequest(request);
    }
}
