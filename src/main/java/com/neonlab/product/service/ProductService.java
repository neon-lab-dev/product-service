package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.*;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.pojo.ProductDeleteReq;
import com.neonlab.product.repository.ProductRepository;
import com.neonlab.product.utils.MapperUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;



@Service
@Loggable
public class ProductService {

    public final static String BRAND = "non-branded";

    public final static String TAG = "Tags1";

    public final static String MESSAGE = "Product unique code already exists or This Product Already Exists";

    public final static String DELETE_MESSAGE = "Product Quantity Reduce Successfully";

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public ProductDto addProduct(ProductDto productReqDto) throws InvalidInputException, ServerException {

        Optional<Product> existsProduct = productRepository.findByCode(productReqDto.getCode());

        if (existsProduct.isPresent()) {
            throw new ServerException(MESSAGE);
        }

        //productReqDto to Product
        Product product = MapperUtils.map(productReqDto,Product.class);
        product.setBrand(BRAND);
        product.setTags(TAG);
        productRepository.save(product);

        //product to productDto
        return MapperUtils.map(product,ProductDto.class);

    }

    public String deleteProductApi(ProductDeleteReq productDeleteReq) throws  ServerException {

        Product product = productRepository.findByCode(productDeleteReq.getCode())
                .orElseThrow(() -> new ServerException("Product Not found"));

        Integer existsQuantity = product.getQuantity();
        if(existsQuantity>=productDeleteReq.getQuantity()){
            Integer currentQuantity = existsQuantity - productDeleteReq.getQuantity();
            product.setQuantity(currentQuantity);
            productRepository.save(product);
            log.warn("Now Your Product Quantity is {}", currentQuantity);
            return  DELETE_MESSAGE;
        }
        throw new ServerException("Product quantity is not Sufficient");

    }

    @Transactional
    public ProductDto updateProduct(ProductDto product) throws ServerException, InvalidInputException {
        boolean flag = true;
        Product existProducts = productRepository.findByCode(product.getCode())
                .orElseThrow(() -> new ServerException("Product Not found"));

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
            throw new InvalidInputException("Please add atleast one value to update");
        }
        productRepository.save(existProducts);
        return MapperUtils.map(existProducts,ProductDto.class);
    }
}
