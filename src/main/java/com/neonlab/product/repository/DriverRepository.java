package com.neonlab.product.repository;

import com.neonlab.product.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver,String> ,
        JpaSpecificationExecutor<Driver> {
}
