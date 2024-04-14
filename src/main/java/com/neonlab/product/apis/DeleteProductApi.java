package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Objects;



@Service
@Loggable
public class DeleteProductApi {

    @Autowired
    private ProductService productService;

    public ApiOutput<?>deleteProductApi(ProductDeleteReq productDeleteReq){

        try {
            validate(productDeleteReq);
            String status = productService.deleteProductApi(productDeleteReq);
                return new ApiOutput<>(HttpStatus.OK.value(), status, null);
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public void validate(ProductDeleteReq productDeleteReq) throws InvalidInputException, ServerException {

        if(StringUtil.isNullOrEmpty(productDeleteReq.getCode())){
            throw new InvalidInputException("Product code is Mandatory");
        }
        if(Objects.isNull(productDeleteReq.getQuantity())) {
            throw new InvalidInputException("Product Quantity is Mandatory");
        }
        if(!productService.isReduceQuantityValid(productDeleteReq.getCode(),productDeleteReq.getQuantity())){
            throw new InvalidInputException("Product Quantity is not sufficient to reduce");
        }
    }
}
