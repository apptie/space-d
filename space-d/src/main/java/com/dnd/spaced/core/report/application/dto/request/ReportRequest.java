package com.dnd.spaced.core.report.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReportRequest(@Positive Long commentId, @NotBlank String cause) {
}
