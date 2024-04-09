package com.neonlab.product.controller;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.product.entities.Product;
import com.neonlab.product.service.ProductService;
import com.neonlab.product.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Loggable
@RequestMapping("/product/v1")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private Validate validate;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        validate.validate(product);
        return productService.addProduct(product);
    }
}
