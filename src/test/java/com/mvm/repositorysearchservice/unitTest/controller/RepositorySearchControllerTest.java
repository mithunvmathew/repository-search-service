package com.mvm.repositorysearchservice.unitTest.controller;

import com.mvm.repositorysearchservice.controller.RepositorySearchController;
import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.exceptions.SearchTextMissingException;
import com.mvm.repositorysearchservice.service.SearchRepositoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RepositorySearchControllerTest {

    @Mock
    SearchRepositoryService searchRepositoryService;

    @InjectMocks
    RepositorySearchController repositorySearchController;

    @Test
    @DisplayName("Given Search parameters When calling controller Then service will get the converted request")
    public void getSearchRepositoryTest() {
        //Given
        SearchRequest testRequest = new SearchRequest(Optional.of(LocalDate.now()), 1, Optional.of("Python"));
        SearchResponse expectedResponse = new SearchResponse("testRepo", "https://github.com/test",
                10, "Kotlin");
        when(searchRepositoryService.getSearchRepository(testRequest)).thenReturn(List.of(expectedResponse));

        //When
        List<SearchResponse> response = repositorySearchController.getSearchResult(LocalDate.now(), 1,
                "Python");

        //Then
        Mockito.verify(searchRepositoryService).getSearchRepository(testRequest);
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).name(), expectedResponse.name());
        assertEquals(response.get(0).url(), expectedResponse.url());
        assertEquals(response.get(0).programmingLanguage(), expectedResponse.programmingLanguage());
        assertEquals(response.get(0).numberOfStars(), expectedResponse.numberOfStars());

    }

    @Test
    @DisplayName("Given Search parameters with search text missing When calling controller Then service will throw Exception")
    public void getSearchRepositoryWithSearchTexMissingTest() {
        assertThrows(SearchTextMissingException.class, () -> repositorySearchController.getSearchResult(
                null, 1, null));
    }

}
