package com.mvm.repositorysearchservice.remote;

import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.remote.dtos.GithubSearchResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchResponseMapper {
    List<SearchResponse> map(List<GithubSearchResponse.Item> items );
}
