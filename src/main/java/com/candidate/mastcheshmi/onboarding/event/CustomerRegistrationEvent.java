package com.candidate.mastcheshmi.onboarding.event;

import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import org.springframework.context.ApplicationEvent;

/**
 * This event represents the registration of a new customer in the application.
 */
public class CustomerRegistrationEvent extends ApplicationEvent {

    private final Customer customer;

    /**
     * Constructs a new CustomerRegistrationEvent.
     *
     * @param source   The object on which the event initially occurred.
     * @param customer The newly registered customer associated with this event.
     */
    public CustomerRegistrationEvent(Object source, Customer customer) {
        super(source);
        this.customer = customer;
    }

    /**
     * Gets the newly registered customer associated with this event.
     *
     * @return The Customer object representing the newly registered customer.
     */
    public Customer getCustomer() {
        return customer;
    }
}
