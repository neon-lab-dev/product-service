package com.neonlab.product.validation;

import com.neonlab.product.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class Validate {

    public void validate(Product product) throws IllegalArgumentException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the name field.");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the category field.");
        }
        if (product.getSubCategory() == null || product.getSubCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the subCategory field.");
        }
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the code field.");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Please enter the price field.");
        }
        if (product.getDiscountPrice() == null) {
            throw new IllegalArgumentException("Please enter the discountPrice field.");
        }
        if (product.getUnits() == null) {
            throw new IllegalArgumentException("Please enter the units field.");
        }
        if (product.getVariety() == null || product.getVariety().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the variety field.");
        }
        if (product.getQuantity() == null) {
            throw new IllegalArgumentException("Please enter the quantity field.");
        }
        if (product.getTags() == null || product.getTags().trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter the tags field.");
        }
    }
}
