package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@Loggable
public class DeleteProductApi {

    @Autowired
    private ProductService productService;

    public ApiOutput<?>deleteProductApi(String code){
        try {
            validate(code);
            return productService.deleteProductApi(code);
        }catch (InvalidInputException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null);
        }
    }

    public void validate(String code) throws InvalidInputException {
        if(StringUtil.isNullOrEmpty(code)){
            throw new InvalidInputException("Product code is Mandatory");
        }
    }
}
