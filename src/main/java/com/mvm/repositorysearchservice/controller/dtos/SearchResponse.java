package com.mvm.repositorysearchservice.controller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

public record SearchResponse(

        @Schema( type = "string", example = "square/retrofit")
        @NonNull String name,
        @Schema( type = "string", example = "https://github.com/square/retrofit")
        @NonNull String url,

        @Schema( type = "int", example = "10")
        @NonNull int numberOfStars,
        @Schema( type = "string", example = "Java")
        String programmingLanguage
) {}
