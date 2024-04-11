package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ProductNotFoundException;
import com.neonlab.common.expectations.ProductQuantityNotSufficientException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@Loggable
public class DeleteProductApi {

    @Autowired
    private ProductService productService;

    public ApiOutput<?>deleteProductApi(String code,Integer quantity){

        try {
            validate(code,quantity);
            String status = productService.deleteProductApi(code,quantity);
            return new ApiOutput<>(HttpStatus.OK.value(), status,null);
        }catch (InvalidInputException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (ProductNotFoundException e) {
            return new ApiOutput<>(HttpStatus.NO_CONTENT.value(), e.getMessage());
        } catch (ProductQuantityNotSufficientException e) {
            return new ApiOutput<>(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    public void validate(String code, Integer quantity) throws InvalidInputException {

        if(StringUtil.isNullOrEmpty(code)){
            throw new InvalidInputException("Product code is Mandatory");
        }
        Optional.ofNullable(quantity)
                .orElseThrow(() -> new InvalidInputException("Product Quantity is Mandatory"));
    }
}
