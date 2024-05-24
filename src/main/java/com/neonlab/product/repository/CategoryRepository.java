package com.neonlab.product.repository;
import com.neonlab.product.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByType(String type);

}
