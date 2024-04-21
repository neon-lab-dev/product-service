package com.neonlab.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddDriverResponse {
    private String id;
    private String name;
    private String contactNo;
    private String vehicleNo;
    private boolean isAvailable;
}
