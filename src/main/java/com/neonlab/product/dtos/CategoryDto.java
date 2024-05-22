package com.neonlab.product.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neonlab.product.entities.Category;
import com.neonlab.product.enums.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private String name;
    private CategoryType type= CategoryType.ROOT;
    private List<SubCategoryDto> subCategoryDtoList;

}
