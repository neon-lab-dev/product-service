package com.neonlab.product.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReportModel {

    private Long totalProducts;
    private Long totalOutOfStockProducts;

}
