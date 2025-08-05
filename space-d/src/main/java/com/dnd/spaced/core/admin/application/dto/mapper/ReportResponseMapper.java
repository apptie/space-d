package com.dnd.spaced.core.admin.application.dto.mapper;

import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse.ReportResponse;
import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public class ReportResponseMapper {

    public ReportCollectionResponse toDto(List<Report> reports) {
        if (reports.isEmpty()) {
            return new ReportCollectionResponse(List.of(), null);
        }

        List<ReportResponse> reportResponses = reports.stream()
                                                      .map(this::toReportDto)
                                                      .toList();

        return new ReportCollectionResponse(reportResponses, reports.get(reports.size() - 1).getId());
    }

    private ReportResponse toReportDto(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getCommentId(),
                report.getReporterId(),
                report.getReportStatus().getName(),
                report.getReportReason().getCause()
        );
    }
}
