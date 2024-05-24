package com.neonlab.product.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonlab.common.entities.Generic;
import com.neonlab.common.utilities.JsonUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "Category",indexes = {
        @Index(name = "idx_name",columnList = "name")
})
public class Category extends Generic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",unique = true, nullable = false)
    private String name;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> subCategories = new ArrayList<>();

    @Override
    public String toString(){
        return JsonUtils.jsonOf(this);
    }
}
