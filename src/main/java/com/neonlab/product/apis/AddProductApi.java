package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ProductUniqueCodeAlreadyExistsException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.dtos.ProductRequestDto;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Loggable
@Service
public class AddProductApi {
    @Autowired
    private ProductService productService;

    public ApiOutput<ProductDto> createProduct(ProductRequestDto product) {
        try {
            validate(product); // This can throw InvalidInputException

            ProductDto productDto = productService.addProduct(product);
            return new ApiOutput<>(HttpStatus.OK.value(), "Product Added Successfully", productDto);
        }catch (InvalidInputException e) {

            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }catch (ProductUniqueCodeAlreadyExistsException e) {

            return new ApiOutput<>(HttpStatus.ALREADY_REPORTED.value(), e.getMessage(),null);
        }catch (Exception e) {

            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while adding the product", null);
        }
    }


    private void validate (ProductRequestDto product) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(product.getName())){
            throw new InvalidInputException("Name of The Product is Mandatory");
        }
        if(StringUtil.isNullOrEmpty(product.getCategory())){
            throw new InvalidInputException("Name of the product category is Mandatory");
        }
        if(StringUtil.isNullOrEmpty(product.getSubCategory())){
            throw new InvalidInputException("Name of the product sub-category is Mandatory");
        }
        if(StringUtil.isNullOrEmpty(product.getCode())){
            throw new InvalidInputException("Product code is Mandatory");
        }
        if(product.getUnits() == null || StringUtil.isNullOrEmpty(product.getUnits().getUnit())){
            throw new InvalidInputException("Product Units is Mandatory");
        }
        if(StringUtil.isNullOrEmpty(product.getVariety())){
            throw new InvalidInputException("Product Variety is Mandatory");
        }
        Optional.ofNullable(product.getPrice())
                .orElseThrow(() -> new InvalidInputException("Product Price is Mandatory"));
        Optional.ofNullable(product.getQuantity())
                .orElseThrow(() -> new InvalidInputException("Product Quantity is Mandatory"));
    }
}
