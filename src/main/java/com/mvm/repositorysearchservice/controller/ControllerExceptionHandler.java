package com.mvm.repositorysearchservice.controller;

import com.mvm.repositorysearchservice.exceptions.GitHubApiClientException;
import com.mvm.repositorysearchservice.exceptions.SearchTextMissingException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

    record Error(String code, String message) {
    }

    @ExceptionHandler({SearchTextMissingException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
    })
    public ResponseEntity<Error> handleException(Exception exception) {
        return new ResponseEntity<>(new Error(
                "BadRequestException", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GitHubApiClientException.class)
    public ResponseEntity<Error> handleException(GitHubApiClientException exception) {
        return new ResponseEntity<>(new Error(
                "GitHubApiClientException", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({CallNotPermittedException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public void handleCallNotPermittedException() {
    }
}
