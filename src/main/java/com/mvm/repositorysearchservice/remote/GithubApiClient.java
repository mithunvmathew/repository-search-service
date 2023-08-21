package com.mvm.repositorysearchservice.remote;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;

import java.util.List;

public interface GithubApiClient {
    List<SearchResponse> getSearchResult(SearchRequest searchRequest);
}
