package com.candidate.mastcheshmi.onboarding.exceptioin;

public class BadRequestException extends ApiException{
    public BadRequestException(String message) {
        super(message);
    }
}
