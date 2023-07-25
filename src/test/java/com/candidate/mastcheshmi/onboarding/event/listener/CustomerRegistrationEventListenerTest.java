package com.candidate.mastcheshmi.onboarding.event.listener;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.event.CustomerRegistrationEvent;
import com.candidate.mastcheshmi.onboarding.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationEventListenerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerRegistrationEventListener listener;

    @Test
    void onApplicationEvent_customerRegistrationEvent_callsCreateAccount() {
        // Given
        var customer = Customer.builder().id(1l).build();
        CustomerRegistrationEvent event = new CustomerRegistrationEvent(this, customer);

        // When
        listener.onApplicationEvent(event);

        // Then
        verify(accountService, times(1)).createAccount(1L, AccountType.CURRENT, "EUR");
    }
}