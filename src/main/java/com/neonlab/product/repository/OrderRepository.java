package com.neonlab.product.repository;
import com.neonlab.product.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,String> {
}

