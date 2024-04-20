package com.neonlab.product.entities;
import com.neonlab.common.entities.User;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.DriverDetailsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "order", indexes = {
        @Index(name = "idx_user_id",columnList = "user_id"),
        @Index(name = "idx_payment_id",columnList = "payment_id")
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "user_id",nullable = false)
    private String userId;
    @Column(name = "address_id",nullable = false)
    private String addressId;
    @Column(name = "payment_id",nullable = false)
    private String paymentId;
    @Column(name = "bought_product_details",nullable = false)
    private BoughtProductDetailsDto boughtProductDetails;
    @Column(name = "driver_details",nullable = false)
    private DriverDetailsDto driverDetails;
    @Column(name = "total_item_cost",nullable = false)
    private Float totalItemCost;
    @Column(name = "delivery_charges",nullable = false)
    private Float deliveryCharges;
    @Column(name = "total_cost",nullable = false)
    private Float totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

}
