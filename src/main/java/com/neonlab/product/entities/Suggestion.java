package com.neonlab.product.entities;
import com.neonlab.common.entities.Generic;
import com.neonlab.common.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Suggestion extends Generic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "comment",nullable = false)
    private String comment;
}
