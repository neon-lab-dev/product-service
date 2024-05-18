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

@Service
@Loggable
@RequiredArgsConstructor
public class AddCategoryApi {

    private final ValidationUtils validationUtils;
    private final CategoryService categoryService;

    public ApiOutput<CategoryDto> add(CategoryDto categoryDto)  {
        try{
            validate(categoryDto);
            return new ApiOutput<>(HttpStatus.OK.value(), "Category Added Successful",categoryService.add(categoryDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void validate(CategoryDto categoryDto) throws InvalidInputException {
        try {
            validationUtils.validate(categoryDto);
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }

    }
}
