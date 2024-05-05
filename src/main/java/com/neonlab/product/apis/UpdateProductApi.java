
package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.common.validationGroups.UpdateValidationGroup;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Loggable
@Service
public class UpdateProductApi {

    @Autowired
    private ProductService productService;
    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<ProductDto> updateProduct(ProductDto product) {
        try {
            validationUtils.validate(product, UpdateValidationGroup.class);
            for(var variety : product.getVarietyList()){
                validationUtils.validate(variety, UpdateValidationGroup.class);
            }
            ProductDto productDto = productService.update(product);
            return new ApiOutput<>(HttpStatus.OK.value(),"Product Update Successfully",productDto);
        }catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
