package com.neonlab.product.entities;
import com.neonlab.common.entities.Generic;
import com.neonlab.common.entities.User;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.DriverDetailsDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity

@Table(name = "`order`", indexes = {
        @Index(name = "idx_user_id",columnList = "user_id"),
        @Index(name = "idx_payment_id",columnList = "payment_id")
})

public class Order extends Generic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "address_id",nullable = false)
    private String addressId;

    @Column(name = "payment_id",nullable = false)
    private String paymentId;

    @Column(name = "bought_product_details",nullable = false)
    private String boughtProductDetails;

    @Column(name = "driver_details",nullable = false)
    private String driverDetails;

    @Column(name = "total_item_cost",nullable = false)
    private BigDecimal totalItemCost;

    @Column(name = "delivery_charges",nullable = false)
    private BigDecimal deliveryCharges;

    @Column(name = "total_cost",nullable = false)
    private BigDecimal totalCost;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

}
