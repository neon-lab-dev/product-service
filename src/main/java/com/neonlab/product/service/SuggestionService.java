package com.neonlab.product.service;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.entities.User;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.dtos.SuggestionDto;
import com.neonlab.product.entities.Suggestion;
import com.neonlab.product.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class SuggestionService {

    @Autowired
    SuggestionRepository suggestionRepository;
    @Autowired
    UserService userService;

    public ApiOutput<SuggestionDto> createSuggestion(SuggestionDto suggestionDto) throws ServerException, InvalidInputException {
        var suggestion = ObjectMapperUtils.map(suggestionDto, Suggestion.class);
        suggestion.setCreatedBy(getUser().getId());
        suggestion = suggestionRepository.save(suggestion);
        var suggestionDtos = ObjectMapperUtils.map(suggestion,SuggestionDto.class);
        return new ApiOutput<>(HttpStatus.OK.value(), "Thank You for Giving me Suggestion",suggestionDtos);
    }

    private User getUser() throws InvalidInputException {
        return userService.getLoggedInUser();
    }
}
