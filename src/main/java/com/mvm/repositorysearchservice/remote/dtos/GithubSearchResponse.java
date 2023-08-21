package com.mvm.repositorysearchservice.remote.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GithubSearchResponse(List<Item> items) {
    public record Item(
            @JsonProperty("full_name") String name,
            @JsonProperty("html_url") String url,
            @JsonProperty("language") String programmingLanguage,
            @JsonProperty("stargazers_count") int numberOfStars) {
    }
}


