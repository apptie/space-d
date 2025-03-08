package com.dnd.spaced.core.admin.application.event.dto;

import com.dnd.spaced.core.report.domain.enums.ReportStatus;

public record ProcessedReportEvent(ReportStatus reportStatus, Long commentId) {
}
