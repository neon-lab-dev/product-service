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

import java.util.ArrayList;
import java.util.List;


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

    public ApiOutput<List<SuggestionDto>> fetchSuggestion() throws InvalidInputException, ServerException {
        List<Suggestion> suggestionList = suggestionRepository.findByCreatedByOrderByCreatedAtDesc(getUser().getId())
                .orElseThrow(()->new ServerException("Currently You have not give me any Suggestion"));

        List<SuggestionDto>suggestionDtoList = new java.util.ArrayList<>();
        for(var suggestion : suggestionList){
            var suggestionDto = ObjectMapperUtils.map(suggestion,SuggestionDto.class);
            suggestionDtoList.add(suggestionDto);
        }

        return new ApiOutput<>(HttpStatus.OK.value(),"All Your Suggestion is Below" ,suggestionDtoList);
    }

    public ApiOutput<?> deleteSuggestion(List<String> suggestionIds) {
        List<String> message = new ArrayList<>();
        for(var suggestionId : suggestionIds){
            var suggestion = getSuggestionById(suggestionId);
            if(suggestion != null) {
                suggestionRepository.delete(suggestion);
                message.add("Suggestion Deleted Successfully "+suggestion.getId());
            }else{
                message.add("Suggestion is not belong with this id "+suggestionId);
            }
        }
        return new ApiOutput<>(HttpStatus.OK.value(),null,message);
    }

    public Suggestion getSuggestionById(String id){
        return suggestionRepository.findById(id).orElse(null);
    }

    private User getUser() throws InvalidInputException {
        return userService.getLoggedInUser();
    }
}
