package com.neonlab.product.repository;
import com.neonlab.product.entities.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SuggestionRepository extends JpaRepository<Suggestion,String> {
    Optional<List<Suggestion>> findByCreatedByOrderByCreatedAtDesc(String id);
}
