package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
@Loggable
public class DeleteProductApi {

    @Autowired
    private ProductService productService;

    public ApiOutput<?> process(List<String> productIds){

        try {
            validate(productIds);
            String status = productService.deleteProduct(productIds);
                return new ApiOutput<>(HttpStatus.OK.value(), status, true);
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public void validate(List<String> productIds) throws InvalidInputException, ServerException {
        if (CollectionUtils.isEmpty(productIds)){
            throw new InvalidInputException("Product Ids list cannot be empty.");
        }
    }
}
