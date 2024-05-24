package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.dtos.CategoryDto;
import com.neonlab.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class UpdateCategoryApi {
    @Autowired
    private CategoryService categoryService;

    public ApiOutput<CategoryDto> update(CategoryDto categoryDto) {
        try {
            return new ApiOutput<>(HttpStatus.OK.value(),"Category Updated Successfully",categoryService.update(categoryDto));
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
