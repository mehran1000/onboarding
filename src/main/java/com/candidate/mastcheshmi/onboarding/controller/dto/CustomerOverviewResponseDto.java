package com.candidate.mastcheshmi.onboarding.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response object containing an overview of the customer and their accounts.")
public record CustomerOverviewResponseDto(
    @Schema(description = "The name of the customer.", example = "Mehran Mastcheshmi")
    String name,

    @Schema(description = "List of accounts belonging to the customer.")
    List<AccountDto> accounts
) {

}
