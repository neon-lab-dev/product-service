package com.neonlab.product.models.requests;
import lombok.Data;


@Data
public class ProductDeleteReq {
    private String code;
    private Integer quantity;
    private Boolean deleteProduct=false;
}
