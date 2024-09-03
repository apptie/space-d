package com.dnd.spaced.core.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAccountCareerInfoRequest(
        @NotBlank
        String jobGroupName,

        @NotBlank
        String companyName,

        @NotBlank
        String experienceName
) {
}
