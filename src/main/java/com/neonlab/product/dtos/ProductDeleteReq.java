package com.neonlab.product.dtos;
import lombok.Data;


@Data
public class ProductDeleteReq {
    private String code;
    private Integer quantity;
}
