package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.expectations.*;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.enums.Units;
import com.neonlab.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;



@Service
@Loggable
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDto addProduct(ProductDto productReqDto) throws ProductUniqueCodeAlreadyExistsException, InvalidInputException {

        //Boolean isExist = productRepository.existsByCode(productReqDto.getCode());

        Optional<Product> existsProduct = productRepository.findByCode(productReqDto.getCode());
        if (existsProduct.isPresent()) {
            throw new ProductUniqueCodeAlreadyExistsException("Product unique code already exists or This Product Already Exists");
        }
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
            //product.setTags(productReqDto.getTags());
            product.setVariety(productReqDto.getVariety());
            productRepository.save(product);
            return ProductDto.parse(product);

        }catch (InvalidInputException e) {
            // Throw an internal server error exception
            throw new InvalidInputException("Invalid Input Exception.");
        }
        /*
        catch (ServerException e){
            throw new ServerException("Internal Server Exception");
        }

         */

    }

    public String deleteProductApi(String code,Integer quantity) throws ProductNotFoundException, ProductQuantityNotSufficientException {

        try {
            Optional<Product> product = Optional.ofNullable(productRepository.findByCode(code)
                    .orElseThrow(() -> new ProductNotFoundException("Product Not found")));

            Integer existsQuantity = product.get().getQuantity();
            if(existsQuantity>=quantity){
                Integer currentQuantity = existsQuantity - quantity;
                product.get().setQuantity(currentQuantity);
                productRepository.save(product.get());
                return  "Product Quantity Reduce Successfully , Now product quantity is - "+currentQuantity;
            }
            throw new ProductQuantityNotSufficientException("Product quantity is not Sufficient");

        }catch (ProductNotFoundException e){
            throw  new ProductNotFoundException(e.getMessage());
        } catch (ProductQuantityNotSufficientException e) {
            throw new ProductQuantityNotSufficientException(e.getMessage());
        }
    }
}
