package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.product.apis.*;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.models.requests.UpdateOrderRequest;
import com.neonlab.product.models.searchCriteria.OrderSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderApi createOrderApi;
    private final UpdateOrderApi updateOrderApi;
    private final CancelOrderApi cancelOrderApi;
    private final FetchOrderApi fetchOrderApi;
    private final EvaluateOrderApi evaluateOrderApi;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> create(@RequestBody OrderDto orderDto) {
        return createOrderApi.create(orderDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> updateOrder(@RequestBody UpdateOrderRequest request){
        return updateOrderApi.process(request);
    }

    @DeleteMapping("/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> cancelOrder(@RequestParam(value = "orderId") String orderId){
        return cancelOrderApi.process(orderId);
    }

    @GetMapping("/admin/list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<?> getAllOrder(final OrderSearchCriteria searchCriteria) throws InvalidInputException {
        searchCriteria.setAdmin(true);
        return fetchOrderApi.process(searchCriteria);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> getOrder(final OrderSearchCriteria searchCriteria) throws InvalidInputException {
        searchCriteria.setAdmin(false);
        return fetchOrderApi.process(searchCriteria);
    }

    @GetMapping("/evaluate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> evaluateOrder(final @RequestBody OrderDto orderDto){
        return evaluateOrderApi.evaluate(orderDto);
    }
}
