package com.dnd.spaced.core.report.domain.repository;

import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import java.util.List;
import java.util.Optional;

public interface ReportRepository {

    void save(Report report);

    Optional<Report> findBy(Long reportId);

    List<Report> findAllBy(ReportStatus reportStatus, Long lastReportId);
}
