package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class GetAllCategoryApi {
    @Autowired
    CategoryService categoryService;

    public ApiOutput<?> getAll() {
        try {
            return new ApiOutput<>(HttpStatus.OK.value(), "All categories is below", categoryService.getAll());
        }catch (ServerException e){
            return null;
        }
    }
}
