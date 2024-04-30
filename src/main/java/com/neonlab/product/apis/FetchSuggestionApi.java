package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.product.dtos.SuggestionDto;
import com.neonlab.product.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FetchSuggestionApi {

    @Autowired
    SuggestionService suggestionService;

    public ApiOutput<List<SuggestionDto>> fetchSuggestion() {
        try{
            return suggestionService.fetchSuggestion();
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
