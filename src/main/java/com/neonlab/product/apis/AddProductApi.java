package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.DTO.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
@Loggable
@Service
public class AddProductApi {
    @Autowired
    private ProductService productService;

    public ApiOutput<ProductDto>createProduct(@RequestBody Product product) throws InvalidInputException {
        validate(product);
        ProductDto productDto = productService.addProduct(product);
        return new ApiOutput<>(HttpStatus.CREATED.value(),"Product Added Successfull",productDto);
    }

    private void validate (Product product) throws InvalidInputException {
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
        if(StringUtil.isNullOrEmpty(product.getUnits().getUnit())){
            throw new InvalidInputException("Product Units is Mandatory");
        }
        if(StringUtil.isNullOrEmpty(product.getVariety())){
            throw new InvalidInputException("Product Variety is Mandatory");
        }
        if(product.getPrice() == null){
            throw new InvalidInputException("Product Price is Mandatory");
        }
        if(product.getDiscountPrice() == null){
            throw new InvalidInputException("Product Discount Price is Mandatory");
        }
        if(product.getQuantity() == null){
            throw new InvalidInputException("Product Quantity is Mandatory");
        }
    }
}
