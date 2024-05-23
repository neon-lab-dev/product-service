package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.AddCategoryApi;
import com.neonlab.product.apis.UpdateCategoryApi;
import com.neonlab.product.dtos.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Loggable
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final AddCategoryApi addCategoryApi;
    private final UpdateCategoryApi updateCategoryApi;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<CategoryDto> add(@ModelAttribute CategoryDto categoryDto){
        return addCategoryApi.add(categoryDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<CategoryDto> update(@ModelAttribute CategoryDto categoryDto){
        return updateCategoryApi.update(categoryDto);
    }

}
