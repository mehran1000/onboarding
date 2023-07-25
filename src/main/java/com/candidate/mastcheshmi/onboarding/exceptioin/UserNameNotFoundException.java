package com.candidate.mastcheshmi.onboarding.exceptioin;

public class UserNameNotFoundException extends NotFoundException{

    public UserNameNotFoundException(String userName) {
        super(String.format("The customer with %s userName does not exist.", userName));
    }
}
