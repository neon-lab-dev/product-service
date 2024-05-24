package com.neonlab.product.apis;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.StringUtil;
import com.neonlab.common.utilities.ValidationUtils;
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
    private ValidationUtils validationUtils;
    @Autowired
    private CategoryService categoryService;
    public ApiOutput<?> get(String name){
//        try {
//            List<CategoryDto> retVal = categoryService.get(name);
            return new ApiOutput<>(HttpStatus.OK.value(), null,null);
//        } catch (ServerException e) {
          //  return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());

    }

}
