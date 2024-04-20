package com.neonlab.product.service;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.controller.ProductController;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductService productService;
    @Autowired
    ProductController productController;

    public ApiOutput<?> createOrder(OrderDto orderDto) throws InvalidInputException, ServerException {
        BigDecimal totalItemCost;
        for(String code : orderDto.getProductCodeAndQuantity().keySet()){
            Product product = productService.fetchProductByCode(code);
            Integer qty = orderDto.getProductCodeAndQuantity().get(code);
            var productDeleteReq = new ProductDeleteReq();
            productDeleteReq.setQuantity(qty);
            productDeleteReq.setCode(code);
            int statusCode = productController.deleteProduct(productDeleteReq).getStatusCode();
            if(statusCode == 200){
                BigDecimal price = product.getPrice();
                totalItemCost = price.multiply(BigDecimal.valueOf(qty));
                var boughtProduct = new BoughtProductDetailsDto();
                boughtProduct = ObjectMapperUtils.map(product,BoughtProductDetailsDto.class);
                return new ApiOutput<>(HttpStatus.OK.value(), "Product Purchase Successfully",boughtProduct);
            }
        }

        return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), "Product Out Of Stock");
    }
}
