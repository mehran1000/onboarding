package com.candidate.mastcheshmi.onboarding.exceptioin;

/**
 * This class is designed to standardize the exceptions that are related to the API calls.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
