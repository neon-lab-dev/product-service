package com.neonlab.product.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonlab.common.entities.Generic;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.product.enums.Units;
import com.neonlab.product.enums.VarietyType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "variety")
@NoArgsConstructor
public class Variety extends Generic {

    public Variety(String createdBy, String modifiedBy){
        super(createdBy,modifiedBy);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private VarietyType type;
    @Column(name = "value", nullable = false)
    private String value;
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private Units unit;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "price",nullable = false)
    private BigDecimal price;
    @Column(name = "discount_percent", columnDefinition = "decimal(38,2) default 0.0")
    private BigDecimal discountPercent;
    @Column(name = "discount_price", columnDefinition = "decimal(38,2) default 0.0")
    private BigDecimal discountPrice;
    @Column(name = "quantity",nullable = false)
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Override
    public String toString(){
        return JsonUtils.jsonOf(this);
    }

}
