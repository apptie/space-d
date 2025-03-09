package com.dnd.spaced.core.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeCareerInfoRequest(
        @NotBlank
        String changedJobGroupName,

        @NotBlank
        String changedCompanyName,

        @NotBlank
        String changedExperienceName
) {
}
