package com.candidate.mastcheshmi.onboarding.controller.mapper;

import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerOverviewResponseDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationRequestDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationResponseDto;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRegistrationMapper {

    Customer mapToCustomer(CustomerRegistrationRequestDto customerRegistrationRequestDto);

    @Mapping(source = "plainPassword", target = "password")
    CustomerRegistrationResponseDto mapToRegistrationResponse(Customer customer);

    CustomerOverviewResponseDto mapToCustomerOverview(Customer customer);
}
