package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.SuggestionApi;
import com.neonlab.product.dtos.SuggestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/suggestion")
public class SuggestionController {

    private final SuggestionApi suggestionApi;

    @PostMapping("/create")
    public ApiOutput<SuggestionDto> createSuggestion(@RequestBody SuggestionDto suggestionDto){
        return suggestionApi.createSuggestion(suggestionDto);
    }
}
