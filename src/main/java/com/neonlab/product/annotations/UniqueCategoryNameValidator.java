package com.neonlab.product.annotations;
import com.neonlab.product.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, String> {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize(UniqueCategoryName constraintAnnotation) {}

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return true;
            }
            boolean exists = categoryRepository.existsByName(name);
            return !exists;
        } catch (Exception e) {
            return false;
        }
    }
}
