package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.CategoryDto;
import com.neonlab.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Loggable
@RequiredArgsConstructor
public class AddCategoryApi {

    private final CategoryService categoryService;
    private final ValidationUtils validationUtils;

    public ApiOutput<CategoryDto> add(CategoryDto categoryDto)  {
        try{
            validationUtils.validate(categoryDto);
            return new ApiOutput<>(HttpStatus.OK.value(), "Category Added Successful",categoryService.add(categoryDto));
        } catch (Exception e) {
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

}
