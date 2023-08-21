package com.mvm.repositorysearchservice.unitTest.controller;

import com.mvm.repositorysearchservice.controller.ControllerExceptionHandler;
import com.mvm.repositorysearchservice.controller.RepositorySearchController;
import com.mvm.repositorysearchservice.exceptions.GitHubApiClientException;
import com.mvm.repositorysearchservice.exceptions.SearchTextMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerExceptionHandlerTest {

    private MockMvc mvc;

    @Mock
    private RepositorySearchController repositorySearchController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(repositorySearchController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }


    @Test
    public void searchTextMissingTest() throws Exception {
        doThrow(SearchTextMissingException.class).when(repositorySearchController).getSearchResult(
                null, 10, null);
        mvc.perform(get("/search/repositories?resultCount=10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void gitHubApiClientExceptionTest() throws Exception {
        doThrow(GitHubApiClientException.class).when(repositorySearchController).getSearchResult(
                null, 10, "Java");
        mvc.perform(get("/search/repositories?resultCount=10&programmingLanguage=Java"))
                .andExpect(status().isInternalServerError());
    }
}
