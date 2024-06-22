package com.neonlab.product.models.responses;

import com.neonlab.common.enums.OrderStatus;
import com.neonlab.common.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportModel {

    private Long totalOrders;
    private Map<OrderStatus, Long> countPerStatus;
    private Map<PaymentMode, Long> countPerPaymentMode;
}
