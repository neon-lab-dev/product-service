package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Objects;


@Loggable
@Service
public class UpdateProductApi {

    @Autowired
    private ProductService productService;

    public ApiOutput<ProductDto> updateProduct(ProductDto product) {

        try {
            validate(product);
            ProductDto productDto = productService.updateProduct(product);
            return new ApiOutput<>(HttpStatus.OK.value(),"Product Update Successfully",productDto);
        }catch (InvalidInputException | ServerException e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private void validate(ProductDto productDto) throws InvalidInputException {
        if(Objects.isNull(productDto)){
            throw new NullPointerException("Please Add Something To update Product");
        }
        if(StringUtil.isNullOrEmpty(productDto.getCode())){
            throw new InvalidInputException("Product Code is Mandatory");
        }
    }
}
