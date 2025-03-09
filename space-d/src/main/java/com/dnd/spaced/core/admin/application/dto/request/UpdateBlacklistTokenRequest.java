package com.dnd.spaced.core.admin.application.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateBlacklistTokenRequest(@Positive Long accountId) {
}
