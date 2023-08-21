package com.mvm.repositorysearchservice.integrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.mvm.repositorysearchservice.RepositorySearchServiceApplication;
import com.mvm.repositorysearchservice.controller.dtos.SearchResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RepositorySearchServiceApplication.class})
@Tag("integrationTest")
class RepositorySearchServiceApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    private String BASIC_SERVER_PATH = "/search/repositories";
    static WireMockServer mockServer;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        mockServer = new WireMockServer(
                new WireMockConfiguration()
                        .port(18085)
                        .usingFilesUnderClasspath("wiremock")
                        .extensions(new ResponseTemplateTransformer(true))
        );
        mockServer.start();
    }


    @Test
    @DisplayName("Given Search request parameters When calling the service  Then service will return response")
    void getRepositoriesFromSearchWithRequestedTest() throws IOException {
        //Given
        String url = BASIC_SERVER_PATH + "?resultCount=2&programmingLanguage=JavaScript";
        HttpHeaders header = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(header);

        //When
        ResponseEntity response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        //Then
        List<SearchResponse> result = objectMapper.readValue(response.getBody().toString(), new TypeReference<>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, result.size());
        assertEquals("JavaScript", result.get(0).programmingLanguage());
        assertEquals("ubilabs/geocomplete", result.get(0).name());
        assertEquals(1229, result.get(0).numberOfStars());
        assertEquals("https://github.com/ubilabs/geocomplete", result.get(0).url());
        assertEquals("JavaScript", result.get(1).programmingLanguage());
    }

    @Test
    @DisplayName("Given Search request parameters When calling the service with git hub api is down  Then service will return Error response")
    void getExceptionWhenGithubApiIsDownTest() throws IOException {

        //Given
        String url = BASIC_SERVER_PATH + "?resultCount=10&programmingLanguage=Java";
        HttpHeaders header = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(header);

        //When
        ResponseEntity response = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @AfterAll
    static void tearDown() {
        mockServer.shutdown();
    }


}
