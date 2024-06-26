package com.neonlab.product.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.common.validationGroups.UpdateValidationGroup;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    @NotEmpty(groups = UpdateValidationGroup.class, message = "Product Id is mandatory")
    private String id;
    @NotEmpty(message = "Product name is mandatory.")
    private String name;
    @NotEmpty(message = "Product code is mandatory.")
    private String code;
    @NotEmpty(message = "Product category is mandatory.")
    private String category;
    @NotEmpty(message = "Product sub-category is mandatory.")
    private String subCategory;
    @NotEmpty(message = "Product sub-category2 is mandatory.")
    private String subCategory2;
    @NotEmpty(message = "Product description is mandatory.")
    private String description;
    private String brand;
    private List<MultipartFile> documents;
    private List<String> documentUrls;
    private List<VarietyDto> varietyList;
}
