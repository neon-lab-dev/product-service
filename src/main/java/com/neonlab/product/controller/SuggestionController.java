package com.neonlab.product.controller;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.DeleteSuggestionApi;
import com.neonlab.product.apis.FetchSuggestionApi;
import com.neonlab.product.apis.GetAllSuggestionApi;
import com.neonlab.product.apis.SuggestionApi;
import com.neonlab.product.dtos.SuggestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/suggestion")
public class SuggestionController {

    private final SuggestionApi suggestionApi;
    private final FetchSuggestionApi fetchSuggestionApi;
    private final DeleteSuggestionApi deleteSuggestionApi;
    private final GetAllSuggestionApi getAllSuggestionApi;

    @PostMapping("/create")
    public ApiOutput<SuggestionDto> create(@RequestBody SuggestionDto suggestionDto){
        return suggestionApi.create(suggestionDto);
    }

    @GetMapping("all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> fetchByCreatedBy(){
        return fetchSuggestionApi.fetchByCreatedBy();
    }

    @GetMapping("list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiOutput<?> getAll(){
        return getAllSuggestionApi.getAll();
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiOutput<?> delete(@RequestBody List<String> suggestionId){
        return deleteSuggestionApi.delete(suggestionId);
    }
}
