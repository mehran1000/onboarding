package com.candidate.mastcheshmi.onboarding.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "The logon request.")
public record LogonRequestDto(
    @NotBlank(message = "The given userName cannot be blank.")
    @Schema(description = "The name of the customer.", example = "mastcheshmi")
    String userName,

    @NotBlank(message = "The given password cannot be blank.")
    @Schema(description = "The password of the customer.", example = "XUl6n!F8(J")
    String password
) {

}
