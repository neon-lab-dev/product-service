package com.neonlab.product.repository;
import com.neonlab.product.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Optional<Category> findByName(String name);
}
