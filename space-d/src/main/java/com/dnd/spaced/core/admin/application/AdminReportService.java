package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.dto.AdminApplicationMapper;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.event.dto.ProcessedReportEvent;
import com.dnd.spaced.core.admin.application.exception.ReportNotFoundException;
import com.dnd.spaced.core.admin.application.exception.ReportStatusNotFoundException;
import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void process(Long reportId, ProcessReportRequest request) {
        Report report = findReport(reportId);
        ReportStatus reportStatus = findReportStatus(request);

        report.process(reportStatus);
        publishProcessedReportEvent(reportStatus, report);
    }

    public ReportCollectionResponse findAllBy(ReadAllReportSearchRequest request, Pageable pageable) {
        ReportStatus reportStatus = findReportStatus(request);
        List<Report> reports = findAllReportsBy(request, reportStatus, pageable);

        return AdminApplicationMapper.toDto(reports);
    }

    private Report findReport(Long reportId) {
        return reportRepository.findBy(reportId)
                               .orElseThrow(
                                       () -> new ReportNotFoundException(
                                               "지정한 신고 식별자로 신고 내역을 찾을 수 없습니다."
                                       )
                               );
    }

    private ReportStatus findReportStatus(ProcessReportRequest request) {
        return ReportStatus.findBy(request.reportStatus())
                           .orElseThrow(
                                   () -> new ReportStatusNotFoundException(
                                           "지정한 신고 상태를 찾을 수 없습니다."
                                   )
                           );
    }

    private void publishProcessedReportEvent(ReportStatus reportStatus, Report report) {
        eventPublisher.publishEvent(new ProcessedReportEvent(reportStatus, report.getCommentId()));
    }

    private ReportStatus findReportStatus(ReadAllReportSearchRequest request) {
        return ReportStatus.findBy(request.reportStatus())
                           .orElse(null);
    }

    private List<Report> findAllReportsBy(
            ReadAllReportSearchRequest request,
            ReportStatus reportStatus,
            Pageable pageable
    ) {
        return reportRepository.findAllBy(reportStatus, request.lastReportId(), pageable);
    }
}
