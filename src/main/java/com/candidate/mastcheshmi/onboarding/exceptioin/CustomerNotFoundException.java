package com.candidate.mastcheshmi.onboarding.exceptioin;

public class CustomerNotFoundException extends NotFoundException{

    public CustomerNotFoundException(long customerId) {
        super(String.format("The customer with %d id does not exist.", customerId));
    }
}
