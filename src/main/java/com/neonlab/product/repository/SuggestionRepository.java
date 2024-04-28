package com.neonlab.product.repository;
import com.neonlab.product.entities.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SuggestionRepository extends JpaRepository<Suggestion,String> {
}
