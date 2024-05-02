package com.neonlab.product.entities;
import com.neonlab.common.entities.Generic;
import com.neonlab.common.entities.User;
import com.neonlab.product.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "`order`", indexes = {
        @Index(name = "idx_user_id",columnList = "user_id"),
        @Index(name = "idx_payment_id",columnList = "payment_id")
})

public class Order extends Generic {

    public Order(String createdBy, String modifiedBy){
        super(createdBy, modifiedBy);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "address_id", nullable = false)
    private String addressId;

    @Column(name = "bought_product_details", columnDefinition = "json", nullable = false)
    private String boughtProductDetails;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "driver_Id")
    private String driverId;

    @Column(name = "total_item_cost", nullable = false)
    private BigDecimal totalItemCost;

    @Column(name = "delivery_charges", nullable = false)
    private BigDecimal deliveryCharges;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    private String remark;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

}