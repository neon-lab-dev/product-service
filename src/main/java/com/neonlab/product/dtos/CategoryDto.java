package com.neonlab.product.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.product.annotations.UniqueCategoryName;
import com.neonlab.product.enums.CategoryType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    @NotEmpty(message = "Category Name Must not be empty")
    private String name;
    private String type= CategoryType.NON_ROOT.getType();
    private MultipartFile document;
    private String documentUrl;
    @NotEmpty(message = "Subcategory must not be empty or null")
    @Valid
    private List<SubCategoryDto> subCategoryDtoList;
}
