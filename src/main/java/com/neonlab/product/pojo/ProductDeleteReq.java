package com.neonlab.product.pojo;
import lombok.Data;


@Data
public class ProductDeleteReq {
    private String code;
    private Integer quantity;
}
