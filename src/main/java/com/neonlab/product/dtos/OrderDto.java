package com.neonlab.product.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;


@Data
public class OrderDto {
    private String paymentId;
    private Map<String,Integer> productCodeAndQuantity;
    private String addressId;
}
