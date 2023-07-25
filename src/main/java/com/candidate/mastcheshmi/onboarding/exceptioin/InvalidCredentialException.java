package com.candidate.mastcheshmi.onboarding.exceptioin;

public class InvalidCredentialException extends BadRequestException {

    public InvalidCredentialException() {
        super("The userName and/or password are invalid.");
    }
}
