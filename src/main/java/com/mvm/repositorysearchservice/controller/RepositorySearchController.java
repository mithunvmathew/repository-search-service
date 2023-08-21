package com.mvm.repositorysearchservice.controller;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.exceptions.SearchTextMissingException;
import com.mvm.repositorysearchservice.service.SearchRepositoryService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/search/repositories")
public class RepositorySearchController {

    private final SearchRepositoryService searchRepositoryService;

    @Autowired
    public RepositorySearchController(SearchRepositoryService searchRepositoryService) {
        this.searchRepositoryService = searchRepositoryService;
    }

    @GetMapping
    public List<SearchResponse> getSearchResult(
            @Schema(type = "string", example = "2020-01-01")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate createdFrom,
            @RequestParam(required = false, defaultValue = "50")
            @Max(value = 100, message = "expected result count in between 1 to 100")
            @Min(value = 1, message = "expected result count in between 1 to 100")
            int resultCount,
            @Schema(type = "string", example = "Java")
            @RequestParam(required = false) String programmingLanguage
    ) {
        ifAnySearchTextPresent(createdFrom, programmingLanguage);
        return searchRepositoryService.getSearchRepository(
                new SearchRequest(
                        Optional.ofNullable(createdFrom),
                        resultCount,
                        Optional.ofNullable(programmingLanguage)));
    }

    private void ifAnySearchTextPresent(LocalDate createdFrom, String programmingLanguage) {
        if (createdFrom == null && programmingLanguage == null)
            throw new SearchTextMissingException("createdFrom or programmingLanguage must be selected");
    }
}
