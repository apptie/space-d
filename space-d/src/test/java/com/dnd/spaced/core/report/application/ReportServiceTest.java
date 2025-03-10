package com.dnd.spaced.core.report.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.report.application.dto.request.ReportRequest;
import com.dnd.spaced.core.report.application.exception.CannotReportOwnCommentException;
import com.dnd.spaced.core.report.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.report.application.exception.ReportReasonNotFoundException;
import com.dnd.spaced.core.report.application.helper.WithCommentTestHelper;
import com.dnd.spaced.core.report.domain.Report;
import com.dnd.spaced.core.report.domain.repository.ReportRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReportServiceTest extends WithCommentTestHelper {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Test
    void 신고_대상_댓글이_없으면_신고_할_수_없다() {
        // given
        ReportRequest request = new ReportRequest(-999L, "기타");

        // when & then

        assertThatThrownBy(() -> reportService.report(1L, request))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("신고하려는 댓글을 찾을 수 없습니다.");
    }

    @Test
    void 지정한_댓글을_지정한_사유로_신고한다() {
        // given
        ReportRequest request = new ReportRequest(comment.getId(), "광고 및 홍보성 내용");

        // when
        reportService.report(2L, request);

        // then
        List<Report> actual = reportRepository.findAllBy(
                null,
                null,
                Pageable.ofSize(10)
        );

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getCommentId()).isEqualTo(comment.getId()),
                () -> assertThat(actual.get(0).getReporterId()).isEqualTo(2L),
                () -> assertThat(actual.get(0).getReportReason().getCause()).isEqualTo("광고 및 홍보성 내용")
        );
    }

    @ParameterizedTest(name = "신고 사유가 {0}이라면 신고 할 수 없다.")
    @NullAndEmptySource
    void 신고_사유를_찾을_수_없다면_신고_할_수_없다(String invalidCause) {
        // given
        ReportRequest request = new ReportRequest(1L, invalidCause);

        // when & then
        assertThatThrownBy(() -> reportService.report(2L, request))
                .isInstanceOf(ReportReasonNotFoundException.class)
                .hasMessage("지정한 원인의 신고 사유를 찾지 못했습니다.");
    }

    @Test
    void 자신이_작성한_댓글은_신고_할_수_없다() {
        // given
        ReportRequest request = new ReportRequest(comment.getId(), "기타");

        // when & then
        assertThatThrownBy(() -> reportService.report(comment.getAccountId(), request))
                .isInstanceOf(CannotReportOwnCommentException.class)
                .hasMessage("자신이 작성한 댓글은 신고할 수 없습니다.");
    }
}
