package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.DocumentDto;
import com.neonlab.common.expectations.*;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.models.responses.PageableResponse;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import com.neonlab.product.repository.ProductRepository;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.repository.specifications.ProductSpecifications;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public ProductDto addProduct(ProductDto productReqDto) throws InvalidInputException, ServerException {
        Product product = ObjectMapperUtils.map(productReqDto,Product.class);
        if(productReqDto.getBrand() == null) {
            product.setBrand(BRAND);
        }
        product.setTags(TAG);
        product = productRepository.save(product);
        return ObjectMapperUtils.map(product,ProductDto.class);
    }

    public boolean existingProduct(String code) {
        return productRepository.findByCode(code).isPresent();
    }

    public String deleteProductApi(ProductDeleteReq productDeleteReq) throws  InvalidInputException {
        Product product = fetchProductByCode(productDeleteReq.getCode());
        Integer existsQuantity = product.getQuantity();
        Integer currentQuantity = existsQuantity - productDeleteReq.getQuantity();
        product.setQuantity(currentQuantity);
        productRepository.save(product);
        log.warn("Now Your Product Quantity is {}", currentQuantity);
        return DELETE_MESSAGE;
    }

    public boolean isReduceQuantityValid(String code, Integer quantity) throws InvalidInputException {
        Product product = fetchProductByCode(code);
        return product.getQuantity()>=quantity;
    }

    public String deleteWholeProduct(Product product) {
        productRepository.delete(product);
        return WHOLE_PRODUCT_DELETE_MESSAGE;
    }

    @Transactional
    public ProductDto updateProduct(ProductDto product, DocumentDto images) throws ServerException, InvalidInputException {
        boolean flag = true;
        Product existProducts = fetchProductByCode(product.getCode());
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
        if(product.getDiscountPercent() != null){
            flag = false;
            existProducts.setDiscountPercent(product.getDiscountPercent());
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
        productDto.setDocumentId(images.getId());
        return productDto;
    }

    public Product fetchProductByCode(String code) throws InvalidInputException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInputException("Product not found with code "+code));
    }

    public PageableResponse<ProductDto> fetchProducts(final ProductSearchCriteria searchCriteria){
        var pageable = PageableUtils.createPageable(searchCriteria);
        Page<Product> products = productRepository.findAll(
                ProductSpecifications.buildSearchCriteria(searchCriteria),
                pageable
        );
        var reslutList = products.getContent().stream()
                .map(ProductService::getProductDto)
                .filter(Objects::nonNull)
                .toList();
        return new PageableResponse<>(reslutList, searchCriteria);
    }

    /**
     * takes product entity as input and gives productDto. In case
     * of error return null.
     *
     * @param product product entity
     * @return null or productDto
     */
    private static ProductDto getProductDto(Product product) {
        ProductDto retVal = null;
        try{
            retVal = ObjectMapperUtils.map(product, ProductDto.class);
        } catch (ServerException ignored){}
        return retVal;
    }

}
