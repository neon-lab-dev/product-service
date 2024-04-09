package com.neonlab.product.entities;
import com.neonlab.product.enums.Units;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product", indexes = {
        @Index(name = "idx_name" ,columnList = "name"),
        @Index(name = "idx_category",columnList = "category"),
        @Index(name = "Idx_sub_category",columnList = "sub_category"),
        @Index(name = "idx_brand",columnList = "brand"),
        @Index(name = "idx_code",columnList = "code")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name",unique = true,nullable = false)
    private String name;//not null index unique
    @Column(name = "category",nullable = false)
    private String category;//not null index
    @Column(name = "sub_category",nullable = false)
    private String subCategory;//not null index
    @Column(name = "description",nullable = false)
    private String description;//not null
    @Column(name = "brand")
    private String brand;//nullable
    @Column(name = "code",nullable = false,unique = true)
    private String code;//not null index unique
    @Column(name = "price",nullable = false)
    private Integer price;//not null
    @Column(name = "discount_price",nullable = false)
    private Integer discountPrice;//not null

    @Enumerated(EnumType.STRING)
    private Units units;//not null
    @Column(name = "variety",nullable = false)
    private String variety;//not null
    @Column(name = "quantity",nullable = false)
    private Integer quantity;//not null
    @Column(name = "tags",nullable = false)
    private String tags;//not null
}