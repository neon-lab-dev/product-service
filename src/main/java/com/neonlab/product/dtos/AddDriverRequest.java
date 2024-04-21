package com.neonlab.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AddDriverRequest {
    private String name;
    private String contactNo;
    private String vehicleNo;
}
