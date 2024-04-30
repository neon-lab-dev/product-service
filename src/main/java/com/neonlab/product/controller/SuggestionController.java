package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.DeleteSuggestionApi;
import com.neonlab.product.apis.FetchSuggestionApi;
import com.neonlab.product.apis.SuggestionApi;
import com.neonlab.product.dtos.SuggestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/suggestion")
public class SuggestionController {

    private final SuggestionApi suggestionApi;
    private final FetchSuggestionApi fetchSuggestionApi;
    private final DeleteSuggestionApi deleteSuggestionApi;

    @PostMapping("/create")
    public ApiOutput<SuggestionDto> createSuggestion(@RequestBody SuggestionDto suggestionDto){
        return suggestionApi.createSuggestion(suggestionDto);
    }

    @GetMapping("all")
    public ApiOutput<List<SuggestionDto>> fetchSuggestion(){
        return fetchSuggestionApi.fetchSuggestion();
    }

    @DeleteMapping("/remove")
    public ApiOutput<?> deleteSuggestion(@RequestBody List<String> suggestionId){
        return deleteSuggestionApi.deleteSuggestion(suggestionId);
    }
}
