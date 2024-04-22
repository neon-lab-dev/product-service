package com.neonlab.product.entities;
import com.neonlab.common.entities.User;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.DriverDetailsDto;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Entity

@Table(name = "`order`", indexes = {
        @Index(name = "idx_user_id",columnList = "user_id"),
        @Index(name = "idx_payment_id",columnList = "payment_id")
})

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "address_id",nullable = false)
    private String addressId;

    @Column(name = "payment_id",nullable = false)
    private String paymentId;

    @Embedded
    @Column(name = "bought_product_details",nullable = false)
    private BoughtProductDetailsDto boughtProductDetails;

    @Embedded
    @Column(name = "driver_details",nullable = false)
    private DriverDetailsDto driverDetails;

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
