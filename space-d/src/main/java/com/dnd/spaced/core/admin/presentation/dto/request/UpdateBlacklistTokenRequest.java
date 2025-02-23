package com.dnd.spaced.core.admin.presentation.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateBlacklistTokenRequest(@Positive Long accountId) {
}
