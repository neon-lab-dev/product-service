package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class AddCategoryApi {

    @Autowired
    private ValidationUtils validationUtils;
    public ApiOutput<CategoryDto> add(CategoryDto categoryDto)  {
        try{
            validate(categoryDto);

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
