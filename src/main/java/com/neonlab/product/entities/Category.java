package com.neonlab.product.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonlab.common.entities.Generic;
import com.neonlab.product.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends Generic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> subCategories = new ArrayList<>();
}
