package com.mvm.repositorysearchservice.unitTest.remote;

import com.mvm.repositorysearchservice.controller.dtos.SearchRequest;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import com.mvm.repositorysearchservice.exceptions.GitHubApiClientException;
import com.mvm.repositorysearchservice.remote.GithubApiClientImpl;
import com.mvm.repositorysearchservice.remote.SearchResponseMapper;
import com.mvm.repositorysearchservice.remote.dtos.GithubSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
public class GithubApiClientImplTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    SearchResponseMapper searchResponseMapper;

    @InjectMocks
    GithubApiClientImpl githubApiClient;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(githubApiClient, "baselUrl", "http://testurl");
    }

    @Test
    @DisplayName("Given SearchRequest When calling GithubApiClient Then return ListOf SearchResponse")
    public void getSearchResultFromGithubApiClientTest() {
        //Given
        SearchRequest searchRequest = new SearchRequest(Optional.ofNullable(null), 10, Optional.of("Python"));
        GithubSearchResponse.Item item = new GithubSearchResponse.Item(
                "testRepo", "http://testrepositoryurl", "Python", 100);
        GithubSearchResponse expectedGithubResponse = new GithubSearchResponse(List.of(item));
        SearchResponse searchResponse = new SearchResponse("testRepo", "http://testrepositoryurl", 100, "Python");
        List<SearchResponse> expectedSearchResponse = List.of(searchResponse);
        Mockito.doReturn(expectedGithubResponse).when(restTemplate).getForObject(
                "http://testurl?sort=stars&per_page=10&q=language:Python", GithubSearchResponse.class);
        Mockito.doReturn(expectedSearchResponse).when(searchResponseMapper).map(expectedGithubResponse.items());

        //When
        List<SearchResponse> searchResponses = githubApiClient.getSearchResult(searchRequest);

        //Then
        assertEquals(1, searchResponses.size());
        assertEquals("testRepo", expectedSearchResponse.get(0).name());
        assertEquals(100, expectedSearchResponse.get(0).numberOfStars());
        assertEquals("Python", expectedSearchResponse.get(0).programmingLanguage());
        assertEquals("http://testrepositoryurl", expectedSearchResponse.get(0).url());
    }

    @Test
    @DisplayName("Given SearchRequest When calling GithubApiClient throw Exception Then return GitHubApiClientException")
    public void getExceptionFromGithubApiClientTest() {
        //Given
        SearchRequest searchRequest = new SearchRequest(Optional.ofNullable(null), 10, Optional.of("Python"));

        //When
        doThrow(HttpClientErrorException.class).when(restTemplate).getForObject(
                "http://testurl?sort=stars&per_page=10&q=language:Python", GithubSearchResponse.class);

        //Then
        assertThrows(GitHubApiClientException.class, () -> githubApiClient.getSearchResult(searchRequest));

    }

}
