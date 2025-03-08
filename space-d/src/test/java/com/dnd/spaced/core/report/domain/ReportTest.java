package com.dnd.spaced.core.report.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReportTest {

    @Test
    void 신고를_초기화한다() {
        // when & then
        Report actual = assertDoesNotThrow(() -> new Report(ReportReason.ETC, 1L, 1L));

        assertAll(
                () -> assertThat(actual.getReportReason()).isEqualTo(ReportReason.ETC),
                () -> assertThat(actual.getCommentId()).isEqualTo(1L),
                () -> assertThat(actual.getReporterId()).isEqualTo(1L),
                () -> assertThat(actual.getReportStatus()).isEqualTo(ReportStatus.PENDING)
        );
    }

    @Test
    void 신고를_처리한다() {
        // given
        Report report = new Report(ReportReason.ETC, 1L, 1L);

        // when
        report.process(ReportStatus.PROCESSED);

        // then
        assertThat(report.getReportStatus()).isEqualTo(ReportStatus.PROCESSED);
    }

    @Test
    void 신고_처리_상태인지_확인한다() {
        // given
        Report report = new Report(ReportReason.ETC, 1L, 1L);

        // when
        boolean actual = report.isProcessed();

        // then
        assertThat(actual).isFalse();
    }
}
