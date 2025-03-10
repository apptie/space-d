package com.dnd.spaced.core.admin.application.dto.mapper;

import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse.ReportResponse;
import com.dnd.spaced.core.report.domain.Report;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminApplicationMapper {

    public static ReportCollectionResponse toDto(List<Report> reports) {
        if (reports.isEmpty()) {
            return new ReportCollectionResponse(List.of(), null);
        }

        List<ReportResponse> reportResponses = reports.stream()
                                                      .map(AdminApplicationMapper::toReportDto)
                                                      .toList();

        return new ReportCollectionResponse(reportResponses, reports.get(reports.size() - 1).getId());
    }

    private static ReportResponse toReportDto(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getCommentId(),
                report.getReporterId(),
                report.getReportStatus().name()
        );
    }
}
