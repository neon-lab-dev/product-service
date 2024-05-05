package com.neonlab.product.repository;

import com.neonlab.product.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,String> ,
        JpaSpecificationExecutor<Driver> {

    Optional<Driver> findByContactNo(String contactNo);
    Optional<Driver> findByVehicleNo(String vehicleNo);

}
