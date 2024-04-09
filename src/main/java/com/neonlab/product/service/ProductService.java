package com.neonlab.product.service;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.product.DTO.ProductResponseDTO;
import com.neonlab.product.entities.Product;
import com.neonlab.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> addProduct(Product product) {
        productRepository.save(product);
        Product product1 = new Product();
        ModelMapper modelMapper = new ModelMapper();
        ProductResponseDTO productResponse = modelMapper.map(product1,ProductResponseDTO.class);
        return new ResponseEntity<>(productResponse,HttpStatus.CREATED);
    }
}
