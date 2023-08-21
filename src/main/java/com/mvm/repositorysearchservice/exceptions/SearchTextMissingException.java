package com.mvm.repositorysearchservice.exceptions;

public class SearchTextMissingException extends RuntimeException {
    public SearchTextMissingException(String message) {
        super(message);
    }
}
