package com.neonlab.product.controller;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.apis.UpdateProductApi;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.apis.AddProductApi;
import com.neonlab.product.pojo.ProductDeleteReq;
import com.neonlab.product.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@Loggable
@RequestMapping("/product/v1")
@RequiredArgsConstructor
public class ProductController {

    private final AddProductApi addProductApi;
    private final DeleteProductApi deleteProductApi;
    private final UpdateProductApi updateProductApi;

    @PostMapping("/add")
    public ApiOutput<ProductDto> addProduct(@RequestParam("productDetails") String productJson, @RequestParam("file") MultipartFile file) {

        try {
            ProductDto product = JsonUtil.readObjectFromJson(productJson,ProductDto.class);
            return addProductApi.createProduct(product);
        } catch (ServerException e) {
            // Catch-all for any other unexpected exceptions
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ApiOutput<?>deleteProduct(@RequestBody ProductDeleteReq productDeleteReq){
            return deleteProductApi.deleteProductApi(productDeleteReq);
    }

    @PutMapping("/update")
    public ApiOutput<ProductDto>updateProduct(@RequestParam("ProductDetails") String productJson,@RequestParam("file") MultipartFile file){
        try {
            ProductDto productDto = JsonUtil.readObjectFromJson(productJson, ProductDto.class);
            return updateProductApi.updateProduct(productDto);
        }catch (ServerException e){
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
