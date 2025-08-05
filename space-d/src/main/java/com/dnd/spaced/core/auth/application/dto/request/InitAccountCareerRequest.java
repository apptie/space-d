package com.dnd.spaced.core.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InitAccountCareerRequest(
        @NotBlank
        String jobGroupName,

        @NotBlank
        String companyName,

        @NotBlank
        String experienceName
) {
}
