package com.candidate.mastcheshmi.onboarding.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object to a successful registration.")
public record CustomerRegistrationResponseDto(
    @Schema(description = "The userName of the new customer..", example = "mastcheshmi")
    String userName,

    @Schema(description = "The password of the new customer.", example = "")
    String password) {

}
