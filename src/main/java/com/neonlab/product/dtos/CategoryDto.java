package com.neonlab.product.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonlab.product.entities.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryDto {
    private int id;
    private String name;
    private String type="root";
    private List<SubCategoryDto> subCategoryDtoList =new ArrayList<>();
}
