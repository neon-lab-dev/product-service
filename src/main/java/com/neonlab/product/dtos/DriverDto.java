package com.neonlab.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverDto {
    private String id;
    private String name;
    private String contactNo;
    private String vehicleNo;
    private boolean isAvailable;
}
