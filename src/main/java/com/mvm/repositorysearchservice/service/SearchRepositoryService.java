package com.mvm.repositorysearchservice.service;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;

import java.util.List;

public interface SearchRepositoryService {
    List<SearchResponse> getSearchRepository(SearchRequest searchRequest);
}
