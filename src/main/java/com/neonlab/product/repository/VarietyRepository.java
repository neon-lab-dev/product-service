package com.neonlab.product.repository;

import com.neonlab.product.entities.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VarietyRepository extends JpaRepository<Variety, String>,
        JpaSpecificationExecutor<Variety> {

}
