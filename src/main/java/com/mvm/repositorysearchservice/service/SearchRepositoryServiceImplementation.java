package com.mvm.repositorysearchservice.service;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.remote.GithubApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchRepositoryServiceImplementation implements SearchRepositoryService{


    private final GithubApiClient githubApiClient;

    @Autowired
    public SearchRepositoryServiceImplementation(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @Override
    public List<SearchResponse> getSearchRepository(SearchRequest searchRequest) {
        return githubApiClient.getSearchResult(searchRequest);
    }
}
