package com.candidate.mastcheshmi.onboarding.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object to a logon request.")
public record LogonResponseDto(
    @Schema(description = "The access token that the client can use to call secured services.")
    String accessToken
) {

}
