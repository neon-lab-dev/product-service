package com.neonlab.product.service;
import com.neonlab.common.entities.User;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.UserService;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.product.dtos.SuggestionDto;
import com.neonlab.product.entities.Suggestion;
import com.neonlab.product.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;



@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private UserService userService;

    public SuggestionDto create(SuggestionDto suggestionDto) throws ServerException, InvalidInputException {
        var suggestion = ObjectMapperUtils.map(suggestionDto,Suggestion.class);
        suggestion.setCreatedBy(getUser().getId());
        var suggestions = suggestionRepository.save(suggestion);
        return ObjectMapperUtils.map(suggestions,SuggestionDto.class);
    }

    public List<SuggestionDto> fetchByCreatedBY() throws InvalidInputException, ServerException {
        List<Suggestion> suggestionList = suggestionRepository.findByCreatedByOrderByCreatedAtDesc(getUser().getId())
                .orElseThrow(()->new ServerException("Currently You have not give me any Suggestion"));

        return getSuggestionList(suggestionList);
    }


    public List<String> delete(List<String> suggestionIds) {
        List<String> message = new ArrayList<>();
        for(var suggestionId : suggestionIds){
            var suggestion = getById(suggestionId);
            if(suggestion != null) {
                suggestionRepository.delete(suggestion);
                message.add("Suggestion Deleted Successfully "+suggestion.getId());
            }else{
                message.add("Not Any Suggestion belong with this id "+suggestionId);
            }
        }
        return message;
    }

    public Suggestion getById(String id){
        return suggestionRepository.findById(id).orElse(null);
    }

    private User getUser() throws InvalidInputException {
        return userService.getLoggedInUser();
    }

    public List<SuggestionDto> getAll() throws ServerException {
        List<Suggestion>suggestionList = suggestionRepository.findAll();
        return getSuggestionList(suggestionList);
    }

    private static List<SuggestionDto> getSuggestionList(List<Suggestion> suggestionList) throws ServerException {
        List<SuggestionDto>suggestionDtoList = new ArrayList<>();
        for(var suggestion : suggestionList){
            var suggestionDto = ObjectMapperUtils.map(suggestion,SuggestionDto.class);
            suggestionDtoList.add(suggestionDto);
        }
        return suggestionDtoList;
    }
}
