package com.dnd.spaced.core.report.infrastructure.persistence;

import static com.dnd.spaced.core.report.domain.QReport.report;

import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.dto.ReportInfo;
import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportGatewayRepository implements ReportRepository {

    private static final RowMapper<ReportInfo> reportRowMapper = (rs, rowNum) -> new ReportInfo(
            rs.getLong(1),
            rs.getLong(2),
            rs.getTimestamp(3).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            ReportReason.valueOf(rs.getString(4)),
            ReportStatus.valueOf(rs.getString(5)),
            rs.getLong(6)
    );

    private final JPAQueryFactory queryFactory;
    private final ReportCrudRepository reportCrudRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReportGatewayRepository(
            JPAQueryFactory queryFactory,
            ReportCrudRepository reportCrudRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.queryFactory = queryFactory;
        this.reportCrudRepository = reportCrudRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void save(Report report) {
        reportCrudRepository.save(report);
    }

    @Override
    public Optional<Report> findBy(Long reportId) {
        Report result = queryFactory.selectFrom(report)
                                     .where(report.id.eq(reportId))
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<ReportInfo> findAllBy(ReportStatus reportStatus, Long lastReportId, Pageable pageable) {
        String sql = """
                SELECT
                     r.id,
                     r.comment_id,
                     r.created_at,
                     r.report_reason,
                     r.report_status,
                     r.reporter_id
                FROM
                      (
                         SELECT id
                         FROM reports
                """;

        if (lastReportId != null && reportStatus != null) {
            sql = sql.concat(" WHERE id < :lastReportId");
            sql = sql.concat(" AND report_status = :reportStatus");
        }
        if (lastReportId != null && reportStatus == null) {
            sql = sql.concat(" WHERE id < :lastReportId");
        }
        if (lastReportId == null && reportStatus != null) {
            sql = sql.concat(" WHERE report_status = :reportStatus");
        }

        sql = sql.concat(" ORDER BY id DESC LIMIT :limit) t LEFT JOIN reports r ON r.id = t.id");

        MapSqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("limit", pageable.getPageSize());

        if (lastReportId != null) {
            sqlParameters.addValue("lastReportId", lastReportId);
        }
        if (reportStatus != null) {
            sqlParameters.addValue("reportStatus", reportStatus.name());
        }

        return namedParameterJdbcTemplate.query(sql, sqlParameters, reportRowMapper);
    }
}
