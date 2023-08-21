package com.mvm.repositorysearchservice.remote;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.exceptions.GitHubApiClientException;
import com.mvm.repositorysearchservice.remote.dtos.GithubSearchResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mvm.repositorysearchservice.remote.Constants.*;


@Component
public class GithubApiClientImpl implements GithubApiClient {

    private final RestTemplate restTemplate;
    private final SearchResponseMapper searchResponseMapper;
    private final String baselUrl;

    Logger log = LoggerFactory.getLogger(GithubApiClientImpl.class);

    @Autowired
    public GithubApiClientImpl(RestTemplate restTemplate, SearchResponseMapper searchResponseMapper,
                               @Value("${githubApi.search.url}") String baselUrl) {
        this.restTemplate = restTemplate;
        this.searchResponseMapper = searchResponseMapper;
        this.baselUrl = baselUrl;
    }

    @Override
    @CircuitBreaker(name = "githubApiCircuitBreaker")
    public List<SearchResponse> getSearchResult(SearchRequest searchRequest) {

        String uri = UriComponentsBuilder.fromUriString(baselUrl)
                .queryParamIfPresent(GITHUB_SEARCH_KEY, searchRequest.createdDate().isPresent() ? Optional.of(
                        GITHUB_CREATED_KEY + searchRequest.createdDate().get()) : Optional.empty())
                .queryParam(GITHUB_SEARCH_SORT_KEY, GITHUB_SEARCH_SORT_VALUE)
                .queryParam(GITHUB_SEARCH_PAGE_KEY, searchRequest.resultCount())
                .queryParamIfPresent(GITHUB_SEARCH_KEY, searchRequest.programmingLanguage().isPresent() ? Optional.of(
                        GITHUB_SEARCH_LANGUAGE_KEY + searchRequest.programmingLanguage().get()) : Optional.empty()).toUriString();
        log.debug(uri);
        try {
            return searchResponseMapper.map(Objects.requireNonNull
                    (restTemplate.getForObject(uri, GithubSearchResponse.class)).items());
        } catch (Exception ex) {
            log.error("Exception from GitHubApi:" + ex.getMessage());
            throw new GitHubApiClientException("Git hub api is not responding as per expected");
        }
    }

}
