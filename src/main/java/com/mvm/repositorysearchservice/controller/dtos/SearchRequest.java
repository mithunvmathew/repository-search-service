package com.mvm.repositorysearchservice.controller.dtos;


import java.time.LocalDate;
import java.util.Optional;

public record SearchRequest(
        Optional<LocalDate> createdDate,
        int resultCount,
        Optional<String> programmingLanguage) {
}
