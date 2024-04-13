package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.DocumentDto;
import com.neonlab.common.entities.Document;
import com.neonlab.common.expectations.*;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.dtos.ProductDeleteReq;
import com.neonlab.product.repository.ProductRepository;
import com.neonlab.common.utilities.ObjectMapperUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@Loggable
@Slf4j
public class ProductService {

    public final static String BRAND = "non-branded";

    public final static String TAG = "Tags1";

    public final static String MESSAGE = "Product unique code already exists or This Product Already Exists";

    public final static String DELETE_MESSAGE = "Product Quantity Reduce Successfully";

    public final static String WHOLE_PRODUCT_DELETE_MESSAGE = "Product Deleted Successfully";

    @Autowired
    private ProductRepository productRepository;

    public ProductDto addProduct(ProductDto productReqDto, DocumentDto image) throws InvalidInputException, ServerException {

        Optional<Product> existsProduct = productRepository.findByCode(productReqDto.getCode());

        if (existsProduct.isPresent()) {
            throw new ServerException(MESSAGE);
        }

        //productReqDto to Product
        Product product = ObjectMapperUtils.map(productReqDto,Product.class);
        if(productReqDto.getBrand() == null) {
            product.setBrand(BRAND);
        }
        product.setTags(TAG);
        productRepository.save(product);

        //product to productDto
        var productDto = ObjectMapperUtils.map(product,ProductDto.class);
        productDto.setDocumentId(image.getDocumentId());
        productDto.setDocumentName(image.getDocumentName());
        return productDto;
    }

    public String deleteProductApi(ProductDeleteReq productDeleteReq) throws  InvalidInputException {

        Product product = getProductByCode(productDeleteReq.getCode());
        Integer existsQuantity = product.getQuantity();
        Integer currentQuantity = existsQuantity - productDeleteReq.getQuantity();
        product.setQuantity(currentQuantity);
        productRepository.save(product);
        log.warn("Now Your Product Quantity is {}", currentQuantity);
        return  DELETE_MESSAGE;
    }

    public boolean isReduceQuantityValid(String code, Integer quantity) throws InvalidInputException {
        Product product = getProductByCode(code);
        return product.getQuantity()>=quantity;
    }

    @Transactional
    public ProductDto updateProduct(ProductDto product, DocumentDto images) throws ServerException, InvalidInputException {
        boolean flag = true;
        Product existProducts = getProductByCode(product.getCode());
        if(product.getName() != null){
            flag = false;
            existProducts.setName(product.getName());
        }
        if(product.getQuantity() != null){
            flag = false;
            existProducts.setQuantity(product.getQuantity());
        }
        if(product.getBrand() != null){
            flag = false;
            existProducts.setBrand(product.getBrand());
        }
        if(product.getCategory() != null){
            flag = false;
            existProducts.setCategory(product.getCategory());
        }
        if(product.getSubCategory() != null){
            existProducts.setSubCategory(product.getSubCategory());
            flag = false;
        }
        if(product.getDescription() != null){
            flag = false;
            existProducts.setDescription(product.getDescription());
        }
        if(product.getPrice() != null){
            flag = false;
            existProducts.setPrice(product.getPrice());
        }
        if(product.getDiscountPrice() != null){
            flag = false;
            existProducts.setDiscountPrice(product.getDiscountPrice());
        }
        if(product.getUnits() != null && product.getUnits().getUnit() != null){
            flag = false;
            existProducts.setUnits(product.getUnits());
        }
        if(product.getVariety() != null){
            flag = false;
            existProducts.setVariety(product.getVariety());
        }
        if(flag){
            throw new InvalidInputException("Please add at-least one value to update");
        }
        productRepository.save(existProducts);
        ProductDto productDto = ObjectMapperUtils.map(existProducts,ProductDto.class);
        productDto.setDocumentName(images.getDocumentName());
        productDto.setDocumentId(images.getDocumentId());
        return productDto;
    }

    public String deleteWholeProduct(ProductDeleteReq productDeleteReq) throws InvalidInputException {
        Product product = getProductByCode(productDeleteReq.getCode());
        productRepository.delete(product);
        return WHOLE_PRODUCT_DELETE_MESSAGE;
    }
    private Product getProductByCode(String code) throws InvalidInputException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInputException("Product Not found with code "+code));
    }

}
