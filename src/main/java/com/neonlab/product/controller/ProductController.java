package com.neonlab.product.controller;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.product.DTO.ProductDto;
import com.neonlab.product.apis.AddProductApi;
import com.neonlab.product.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Loggable
@RequestMapping("/product/v1")
@RequiredArgsConstructor
public class ProductController {
    private final AddProductApi addProduct;

    @PostMapping("/add")
    public ApiOutput<ProductDto> addProduct(@RequestBody Product product) throws InvalidInputException {
        return addProduct.createProduct(product);
    }
}
