package com.neonlab.product.annotations;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = UniqueCategoryNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCategoryName {
    String message() default "Category name must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
