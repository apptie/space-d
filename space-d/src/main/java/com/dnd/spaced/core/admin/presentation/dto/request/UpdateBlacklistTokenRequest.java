package com.dnd.spaced.core.admin.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateBlacklistTokenRequest(@NotBlank String accountId) {
}
