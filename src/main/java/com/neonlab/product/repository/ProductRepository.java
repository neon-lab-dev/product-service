package com.neonlab.product.repository;
import com.neonlab.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Product> findByCode(String code);

    Boolean existsByCode(String code);
}
