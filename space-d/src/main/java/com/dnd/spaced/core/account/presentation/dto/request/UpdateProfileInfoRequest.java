package com.dnd.spaced.core.account.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileInfoRequest(@NotBlank String originNickname, @NotBlank String profileImageKoreanName) {
}
