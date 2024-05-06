package com.neonlab.product.models.requests;

import com.neonlab.common.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateOrderRequest {

    @NotEmpty(message = "Order Id is mandatory.")
    private String id;
    private OrderStatus orderStatus;
    private String driverId;

    public static UpdateOrderRequest getCancelOrderRequest(String id){
        var retVal = new UpdateOrderRequest();
        retVal.setId(id);
        retVal.setOrderStatus(OrderStatus.CANCELED);
        retVal.setDriverId(null);
        return retVal;
    }

}
