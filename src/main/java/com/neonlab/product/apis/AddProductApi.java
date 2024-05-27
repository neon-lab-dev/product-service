package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.service.ProductService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Loggable
@Service
public class AddProductApi {

    @Autowired
    private ProductService productService;
    @Autowired
    private ValidationUtils validationUtils;


    public ApiOutput<ProductDto> createProduct(ProductDto productDto) {

        try {
            validate(productDto);
            var retVal = productService.add(productDto);
            return new ApiOutput<>(HttpStatus.OK.value(), "Product Added Successfully", retVal);
        }catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    private void validate (ProductDto product) throws InvalidInputException {
        try {
            validationUtils.validate(product);
        } catch (ConstraintViolationException e) {
            throw new InvalidInputException(e.getMessage());
        }
        if (productService.existingProduct(product.getCode())){
            throw new InvalidInputException("Product already exists with the code "+product.getCode());
        }
        productService.validateCategory(product);
    }
}
