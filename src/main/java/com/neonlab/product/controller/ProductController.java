package com.neonlab.product.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.dtos.ProductDto;
import com.neonlab.product.apis.AddProductApi;
import com.neonlab.product.dtos.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@Loggable
@RequestMapping("/product/v1")
@RequiredArgsConstructor
public class ProductController {

    private final AddProductApi addProductApi;
    private final DeleteProductApi deleteProductApi;

    @PostMapping("/add")
    public ApiOutput<ProductDto> addProduct(@RequestParam("file") MultipartFile file) {
        try {
            var mapper = new ObjectMapper();
            byte[] bytes = file.getBytes();
            var jsonString = new String(bytes);
            ProductRequestDto product = mapper.readValue(jsonString, ProductRequestDto.class);
            return addProductApi.createProduct(product);
        } catch (JsonProcessingException e) {
            // Handle JSON parsing errors
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), "Failed to parse product data from file: " + e.getMessage(), null);
        } catch (IOException e) {
            // Handle errors related to file processing
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to process file: " + e.getMessage(), null);
        }catch (Exception e) {
            // Catch-all for any other unexpected exceptions
            return new ApiOutput<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete")
    public ApiOutput<?>deleteProduct(@RequestParam String code){
            return deleteProductApi.deleteProductApi(code);
    }
}
