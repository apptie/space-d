package com.dnd.spaced.core.admin.application.dto.resposne;

import java.util.List;

public record ReportCollectionResponse(List<ReportResponse> reports, Long lastReportId) {

    public record ReportResponse(Long id, Long commentId, Long reporterId, String reportStatus) {
    }
}
