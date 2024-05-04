package com.neonlab.product.apis;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.utilities.ValidationUtils;
import com.neonlab.product.dtos.SuggestionDto;
import com.neonlab.product.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class SuggestionApi {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<SuggestionDto> create(SuggestionDto suggestionDto) {
        try {
            validationUtils.validate(suggestionDto);
            return new ApiOutput<>(HttpStatus.OK.value(),"Thank Your for your suggestion" ,suggestionService.create(suggestionDto));
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
