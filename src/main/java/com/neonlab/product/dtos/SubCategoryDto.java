package com.neonlab.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class SubCategoryDto {
    private int id;
    private String name;
    private String type="non_root";
    private CategoryDto categoryDto;
    private List<SubCategory2Dto> subCategory2DtoList =new ArrayList<>();
}
