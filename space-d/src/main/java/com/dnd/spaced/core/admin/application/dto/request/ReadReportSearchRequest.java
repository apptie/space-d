package com.dnd.spaced.core.admin.application.dto.request;

import jakarta.annotation.Nullable;

public record ReadReportSearchRequest(@Nullable String reportStatus, @Nullable Long lastReportId) {
}
