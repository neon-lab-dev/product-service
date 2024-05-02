package com.neonlab.product.entities;
import com.neonlab.common.entities.Document;
import com.neonlab.common.entities.Generic;
import com.neonlab.product.enums.Units;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "product", indexes = {
        @Index(name = "idx_name" ,columnList = "name"),
        @Index(name = "idx_category",columnList = "category"),
        @Index(name = "Idx_sub_category",columnList = "sub_category"),
        @Index(name = "idx_brand",columnList = "brand"),
        @Index(name = "idx_code",columnList = "code")
})

public class Product extends Generic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "category",nullable = false)
    private String category;
    @Column(name = "code",nullable = false)
    private String code;
    @Column(name = "sub_category",nullable = false)
    private String subCategory;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "brand")
    private String brand;
    @Column(name = "tags")
    private String tags;//not null

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Variety> varieties;

    public Product(){
        super();
    }

}