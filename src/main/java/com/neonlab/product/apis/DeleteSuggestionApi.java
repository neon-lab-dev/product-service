package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeleteSuggestionApi {

    @Autowired
    private SuggestionService suggestionService;

    public ApiOutput<?> delete(List<String> suggestionId) {
        try{
            return new ApiOutput<>(HttpStatus.OK.value(), null,suggestionService.delete(suggestionId));
        }catch (Exception e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
