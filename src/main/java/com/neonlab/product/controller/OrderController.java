package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.CancelOrderApi;
import com.neonlab.product.apis.CreateOrderApi;
import com.neonlab.product.apis.UpdateOrderApi;
import com.neonlab.product.dtos.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderApi createOrderApi;
    private final UpdateOrderApi updateOrderApi;
    private final CancelOrderApi cancelOrderApi;

    @PostMapping("/create")
    public ApiOutput<?> createOrder(@RequestBody OrderDto orderDto) {
        return createOrderApi.createOrder(orderDto);
    }

    @PutMapping("/update")
    public ApiOutput<?> updateOrder(@RequestParam(value = "orderId") String orderId,
                                    @RequestParam String orderStatus){
        return updateOrderApi.updateOrder(orderId,orderStatus);
    }

    @DeleteMapping("/delete")
    public ApiOutput<?> cancelOrder(@RequestParam(value = "orderId") String orderId){
        return cancelOrderApi.cancelOrder(orderId);
    }
}
