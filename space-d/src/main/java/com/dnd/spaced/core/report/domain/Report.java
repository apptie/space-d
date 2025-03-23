package com.dnd.spaced.core.report.domain;

import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reports")
@Entity
@Getter
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportReason reportReason;

    private Long commentId;

    private Long reporterId;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public Report(ReportReason reportReason, Long commentId, Long reporterId) {
        this.reportReason = reportReason;
        this.commentId = commentId;
        this.reporterId = reporterId;
        this.reportStatus = ReportStatus.PENDING;
    }

    public void process(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public boolean isProcessed() {
        return this.reportStatus == ReportStatus.PROCESSED;
    }
}
