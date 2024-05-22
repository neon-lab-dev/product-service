package com.neonlab.product.controller;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.AddCategoryApi;
import com.neonlab.product.apis.FetchCategoryApi;
import com.neonlab.product.dtos.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Loggable
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final AddCategoryApi addCategoryApi;

    private final FetchCategoryApi fetchCategoryApi;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<CategoryDto> add(@ModelAttribute CategoryDto categoryDto){
        return addCategoryApi.add(categoryDto);
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<?> fetch(@RequestParam String name){
        return fetchCategoryApi.get(name);
    }

}
