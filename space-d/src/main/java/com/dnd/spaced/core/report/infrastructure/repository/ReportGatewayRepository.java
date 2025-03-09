package com.dnd.spaced.core.report.infrastructure.repository;

import static com.dnd.spaced.core.report.domain.QReport.report;

import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportGatewayRepository implements ReportRepository {

    private final JPAQueryFactory queryFactory;
    private final ReportCrudRepository reportCrudRepository;

    @Override
    public void save(Report report) {
        reportCrudRepository.save(report);
    }

    @Override
    public Optional<Report> findBy(Long reportId) {
        return reportCrudRepository.findById(reportId);
    }

    @Override
    public List<Report> findAllBy(ReportStatus reportStatus, Long lastReportId, Pageable pageable) {
        return queryFactory.selectFrom(report)
                           .where(ltLastReportId(lastReportId), eqReportStatus(reportStatus))
                           .orderBy(report.id.desc())
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    private BooleanExpression ltLastReportId(Long lastReportId) {
        if (lastReportId == null) {
            return null;
        }

        return report.id.lt(lastReportId);
    }

    private BooleanExpression eqReportStatus(ReportStatus reportStatus) {
        if (reportStatus == null) {
            return null;
        }

        return report.reportStatus.eq(reportStatus);
    }
}
