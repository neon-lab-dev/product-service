package com.neonlab.product.entities;

import com.neonlab.common.entities.Generic;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "driver", indexes = {@Index(name = "idx_contact_no",columnList = "contact_no")})
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
    private boolean isAvailable;

    public Driver(){
        super();
    }

    public Driver(String name,String contactNo,String vehicleNo){
        super();
        this.name=name;
        this.contactNo=contactNo;
        this.vehicleNo=vehicleNo;
        this.isAvailable=true;
    }

}
