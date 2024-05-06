package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.product.models.searchCriteria.OrderSearchCriteria;
import com.neonlab.product.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
@RequiredArgsConstructor
public class FetchOrderApi {

    private final OrderService orderService;

    public ApiOutput<?> process(final OrderSearchCriteria searchCriteria) throws InvalidInputException {
        return new ApiOutput<>(HttpStatus.OK.value(), null, orderService.fetch(searchCriteria));
    }

}
