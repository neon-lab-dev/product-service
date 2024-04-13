package com.neonlab.product.controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.apis.UpdateProductApi;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.apis.AddProductApi;
import com.neonlab.product.dtos.ProductDeleteReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Loggable
@RequestMapping("/product/v1")
@RequiredArgsConstructor
public class ProductController {

    private final AddProductApi addProductApi;
    private final DeleteProductApi deleteProductApi;
    private final UpdateProductApi updateProductApi;

    @PostMapping("/add")
    public ApiOutput<?> addProduct(
                                @RequestParam("productDetails") String productJson,
                                @RequestParam("files") List<MultipartFile> files) {

        try {
            ProductDto product = JsonUtils.readObjectFromJson(productJson,ProductDto.class);
            return addProductApi.createProduct(product,files);
        } catch (JsonParseException e) {
            return new ApiOutput<>(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ApiOutput<?>deleteProduct(
                        @RequestBody ProductDeleteReq productDeleteReq,
                        @RequestParam boolean removeWholeProduct
                        ){
            return deleteProductApi.deleteProductApi(productDeleteReq,removeWholeProduct);
    }

    @PutMapping("/update")
    public ApiOutput<ProductDto>updateProduct(
                                  @RequestParam("ProductDetails") String productJson,
                                  @RequestParam("files") List<MultipartFile> files,
                                  @RequestParam("id") Long documentId){
        try {

            ProductDto productDto = JsonUtils.readObjectFromJson(productJson, ProductDto.class);
            return updateProductApi.updateProduct(productDto,files,documentId);
        }catch (JsonParseException e){
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
