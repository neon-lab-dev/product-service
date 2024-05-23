package com.neonlab.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.neonlab.common","com.neonlab.product"})
@EnableJpaRepositories(basePackages = {"com.neonlab.common.repositories","com.neonlab.product.repository"})
@EntityScan(basePackages = {"com.neonlab.common.entities","com.neonlab.product.entities"})
public class ProductApplication { public static void main(String[] args) {SpringApplication.run(ProductApplication.class, args);}}
