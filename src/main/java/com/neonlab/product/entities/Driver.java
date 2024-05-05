package com.neonlab.product.entities;

import com.neonlab.common.entities.Generic;
import com.neonlab.product.dtos.DriverDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "driver", indexes = {
        @Index(name = "idx_contact_no",columnList = "contact_no")
})
public class Driver extends Generic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "contact_no",nullable = false)
    private String contactNo;
    @Column(name = "vehicle_no",nullable = false)
    private String vehicleNo;
    @Column(name = "is_available")
    private boolean available;

    public Driver(){
        super();
    }

    public Driver(DriverDto driverDto){
        super();
        this.name= driverDto.getName();
        this.contactNo= driverDto.getContactNo();
        this.vehicleNo= driverDto.getVehicleNo();
        this.available=true;
    }

}
