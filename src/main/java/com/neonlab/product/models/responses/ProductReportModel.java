package com.neonlab.product.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductReportModel {

    private Long totalProducts;
    private Long totalInStockProducts;
    private Long totalOutOfStockProducts;

    public ProductReportModel(Long totalProducts, Long totalOutOfStockProducts){
        this.totalProducts = totalProducts;
        this.totalOutOfStockProducts = totalOutOfStockProducts;
        this.totalInStockProducts = this.totalProducts - this.totalOutOfStockProducts;
    }

}
