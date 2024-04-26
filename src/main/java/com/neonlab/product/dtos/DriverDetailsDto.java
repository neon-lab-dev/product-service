package com.neonlab.product.dtos;
import jakarta.persistence.Embeddable;
import lombok.Data;


@Data
@Embeddable
public class DriverDetailsDto {
    private String driverName;
    private String contactNo;
    private String vehicleNo;
}
