package com.dnd.spaced.core.account.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCareerInfoRequest(
        @NotBlank
        String jobGroupName,

        @NotBlank
        String companyName,

        @NotBlank
        String experienceName
) {
}
