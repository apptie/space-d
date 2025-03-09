package com.dnd.spaced.core.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeProfileInfoRequest(
        @NotBlank
        String changedNickname,

        @NotBlank
        String changedProfileImageKoreanName
) {
}
