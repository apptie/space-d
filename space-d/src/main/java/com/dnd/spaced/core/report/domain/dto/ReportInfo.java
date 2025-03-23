package com.dnd.spaced.core.report.domain.dto;

import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import java.time.LocalDateTime;

public record ReportInfo(
        Long id,
        Long commentId,
        LocalDateTime createdAt,
        ReportReason reportReason,
        ReportStatus reportStatus,
        Long reporterId
) {
}
