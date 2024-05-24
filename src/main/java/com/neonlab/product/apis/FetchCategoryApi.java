package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.dtos.CategoryDto;
import com.neonlab.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Loggable
public class FetchCategoryApi {

    @Autowired
    private CategoryService categoryService;

    public ApiOutput<?> process(String name){
        List<CategoryDto> retVal = categoryService.get(name);
        return new ApiOutput<>(HttpStatus.OK.value(), null,retVal);
    }

}
