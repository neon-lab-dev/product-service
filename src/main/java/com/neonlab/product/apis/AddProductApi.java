package com.neonlab.product.apis;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.services.DocumentService;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.entities.Product;
import com.neonlab.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static com.neonlab.product.constants.ProductConstant.IMAGE_UPLOAD_LIMIT_EXCEEDED;
import java.util.List;
import java.util.Objects;



@Loggable
@Service
public class AddProductApi {
    @Autowired
    private ProductService productService;


    public ApiOutput<ProductDto> createProduct(String productJson, List<MultipartFile> files) {

        try {
            ProductDto product = JsonUtils.readObjectFromJson(productJson , ProductDto.class);
            validate(product,files); // This can throw InvalidInputException
            ProductDto productDto = productService.addProduct(product,files);

            return new ApiOutput<>(HttpStatus.OK.value(), "Product Added Successfully",productDto);
        }catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    private void validate (ProductDto product, List<MultipartFile> files) throws InvalidInputException {

        if(Objects.isNull(product)){
            throw new NullPointerException("Product is Empty or Null Please Add something.");
        }
        if(productService.existingProduct(product.getCode())){
            throw new InvalidInputException(
                    String.format("Product already exists with code %s.", product.getCode())
            );
        }
        if(StringUtil.isNullOrEmpty(product.getName())){
            throw new InvalidInputException("Name of The Product is Mandatory.");
        }
        if(StringUtil.isNullOrEmpty(product.getCategory())){
            throw new InvalidInputException("Name of the product category is Mandatory.");
        }
        if(StringUtil.isNullOrEmpty(product.getSubCategory())){
            throw new InvalidInputException("Name of the product sub-category is Mandatory.");
        }
        if(StringUtil.isNullOrEmpty(product.getCode())){
            throw new InvalidInputException("Product code is Mandatory.");
        }
        if(Objects.isNull(product.getUnits()) || StringUtil.isNullOrEmpty(product.getUnits().getUnit())){
            throw new InvalidInputException("Product Units is Mandatory.");
        }
        if(StringUtil.isNullOrEmpty(product.getVariety())){
            throw new InvalidInputException("Product Variety is Mandatory.");
        }
        if(Objects.isNull(product.getPrice())){
            throw new InvalidInputException("Product Price is Mandatory.");
        }
        if(Objects.isNull(product.getQuantity())){
            throw new InvalidInputException("Product Quantity is Mandatory.");
        }
        if(files.size()>4){
            throw new InvalidInputException(IMAGE_UPLOAD_LIMIT_EXCEEDED);
        }
    }
}
