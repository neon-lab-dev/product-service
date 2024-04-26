//package com.neonlab.product.apis;
//import com.neonlab.common.annotations.Loggable;
//import com.neonlab.common.dto.ApiOutput;
//import com.neonlab.common.expectations.InvalidInputException;
//import com.neonlab.common.utilities.JsonUtils;
//import com.neonlab.common.utilities.StringUtil;
//import com.neonlab.product.dtos.ProductDto;
//import com.neonlab.product.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.util.List;
//import java.util.Objects;
//import static com.neonlab.product.constants.ProductConstant.IMAGE_UPLOAD_LIMIT_EXCEEDED;
//
//
//
//@Loggable
//@Service
//public class UpdateProductApi {
//
//    @Autowired
//    private ProductService productService;
//
//    public ApiOutput<ProductDto> updateProduct(String productJson, List<MultipartFile> files) {
//
//        try {
//            ProductDto product = JsonUtils.readObjectFromJson(productJson , ProductDto.class);
//            validate(product , files);
//            ProductDto productDto = productService.updateProduct(product , files);
//            return new ApiOutput<>(HttpStatus.OK.value(),"Product Update Successfully",productDto);
//        }catch (Exception e) {
//            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        }
//    }
//
//    private void validate(ProductDto productDto , List<MultipartFile> files) throws InvalidInputException {
//        if(Objects.isNull(productDto)){
//            throw new NullPointerException("Please Add Something To update Product");
//        }
//        if(StringUtil.isNullOrEmpty(productDto.getCode())){
//            throw new InvalidInputException("Product Code is Mandatory");
//        }
//
//        if(files != null && files.size()>4){
//            throw new InvalidInputException(IMAGE_UPLOAD_LIMIT_EXCEEDED);
//        }
//
//    }
//}
