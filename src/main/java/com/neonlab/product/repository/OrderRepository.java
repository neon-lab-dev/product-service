
package com.neonlab.product.repository;

import com.neonlab.common.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,String>
        , JpaSpecificationExecutor<Order> {
    Optional<Order> findByPaymentId(String paymentId);

}

