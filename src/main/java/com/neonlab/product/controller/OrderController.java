package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.CancelOrderApi;
import com.neonlab.product.apis.CreateOrderApi;
import com.neonlab.product.apis.UpdateOrderApi;
import com.neonlab.product.dtos.OrderDto;
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

    @PostMapping("/create")
    public ApiOutput<?> create(@RequestBody OrderDto orderDto) {
        return createOrderApi.create(orderDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> updateById(@RequestParam(value = "orderId") String orderId,
                                   @RequestParam String orderStatus){
        return updateOrderApi.updateOrder(orderId,orderStatus);
    }

    @DeleteMapping("/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> cancelById(@RequestParam(value = "orderId") String orderId){
        return cancelOrderApi.cancelById(orderId);
    }
}
