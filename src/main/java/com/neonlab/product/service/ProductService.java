package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.dtos.ProductRequestDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.enums.Units;
import com.neonlab.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Loggable
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDto addProduct(ProductRequestDto productReqDto) {
        try {
            var product = new Product();
            product.setName(productReqDto.getName());
            product.setUnits(Units.fromString(productReqDto.getUnits().unit));
            product.setCode(productReqDto.getCode());
            product.setBrand(productReqDto.getBrand());
            product.setCategory(productReqDto.getCategory());
            product.setSubCategory(productReqDto.getSubCategory());
            product.setDescription(productReqDto.getDescription());
            product.setPrice(productReqDto.getPrice());
            product.setDiscountPrice(productReqDto.getDiscountPrice());
            product.setQuantity(productReqDto.getQuantity());
            product.setTags(productReqDto.getTags());
            product.setVariety(productReqDto.getVariety());

            productRepository.save(product);
            return ProductDto.parse(product);
        } catch (Exception e) {
            // Throw an internal server error exception
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error. Unable to add product.");
        }
    }
}
