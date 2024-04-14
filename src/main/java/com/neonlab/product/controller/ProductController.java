package com.neonlab.product.controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.utilities.JsonUtils;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.apis.FetchProductApi;
import com.neonlab.product.apis.UpdateProductApi;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.apis.AddProductApi;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.models.searchCriteria.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Loggable
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final AddProductApi addProductApi;
    private final DeleteProductApi deleteProductApi;
    private final UpdateProductApi updateProductApi;
    private final FetchProductApi fetchProductApi;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<ProductDto> addProduct(
               @RequestParam("productDetails") String productJson,
               @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        try {
            ProductDto product = JsonUtils.readObjectFromJson(productJson,ProductDto.class);
            return addProductApi.createProduct(product);
        } catch (JsonParseException e) {
            return new ApiOutput<>(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<?>deleteProduct(@RequestBody ProductDeleteReq productDeleteReq){
            return deleteProductApi.deleteProductApi(productDeleteReq);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<ProductDto> updateProduct(
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

    @GetMapping("/list")
    public ApiOutput<?> fetchProducts(final ProductSearchCriteria searchCriteria){
        return fetchProductApi.process(searchCriteria);
    }

}