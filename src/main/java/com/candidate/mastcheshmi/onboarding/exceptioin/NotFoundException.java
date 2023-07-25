package com.candidate.mastcheshmi.onboarding.exceptioin;

public class NotFoundException extends ApiException{
    public NotFoundException(String message) {
        super(message);
    }
}
