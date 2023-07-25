package com.candidate.mastcheshmi.onboarding.exceptioin;

public class UserAlreadyExistException extends BadRequestException {

    public UserAlreadyExistException(String userName) {
        super(String.format("The %s userName already exist.", userName));
    }
}
