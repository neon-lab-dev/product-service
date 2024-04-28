package com.neonlab.product.entities;
import com.neonlab.common.entities.Generic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String comment;
}
