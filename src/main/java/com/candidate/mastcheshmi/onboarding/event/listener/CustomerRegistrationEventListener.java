package com.candidate.mastcheshmi.onboarding.event.listener;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.candidate.mastcheshmi.onboarding.event.CustomerRegistrationEvent;
import com.candidate.mastcheshmi.onboarding.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * This component class listens for the {@link CustomerRegistrationEvent} and handles the account creation for the newly
 * registered customer.
 */
@Component
@RequiredArgsConstructor
public class CustomerRegistrationEventListener implements ApplicationListener<CustomerRegistrationEvent> {

    private final AccountService accountService;

    @Override
    public void onApplicationEvent(CustomerRegistrationEvent event) {
        accountService.createAccount(event.getCustomer().getId(), AccountType.CURRENT, "EUR");
    }
}
