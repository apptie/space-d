package com.dnd.spaced.core.admin.application.dto.mapper;

import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse.ReportResponse;
import com.dnd.spaced.core.report.domain.dto.ReportInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportInfoMapper {

    public static ReportCollectionResponse toDto(List<ReportInfo> reports) {
        if (reports.isEmpty()) {
            return new ReportCollectionResponse(List.of(), null);
        }

        List<ReportResponse> reportResponses = reports.stream()
                                                      .map(ReportInfoMapper::toReportDto)
                                                      .toList();

        return new ReportCollectionResponse(reportResponses, reports.get(reports.size() - 1).id());
    }

    private static ReportResponse toReportDto(ReportInfo report) {
        return new ReportResponse(
                report.id(),
                report.commentId(),
                report.reporterId(),
                report.reportStatus().getName(),
                report.reportReason().getCause()
        );
    }
}
