package com.dnd.spaced.core.account.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileInfoRequest(@NotBlank String nickname, @NotBlank String profileImageKoreanName) {
}
