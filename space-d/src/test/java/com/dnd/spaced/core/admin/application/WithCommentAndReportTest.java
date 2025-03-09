package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.ProcessReportRequest;
import com.dnd.spaced.core.admin.application.dto.request.ReadAllReportSearchRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.ReportCollectionResponse;
import com.dnd.spaced.core.admin.application.event.dto.ProcessedReportEvent;
import com.dnd.spaced.core.admin.application.exception.ReportNotFoundException;
import com.dnd.spaced.core.admin.application.exception.ReportStatusNotFoundException;
import com.dnd.spaced.core.admin.application.helper.WithCommentAndReportTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WithCommentAndReportTest extends WithCommentAndReportTestHelper {

    @Autowired
    AdminReportService adminReportService;

    @Autowired
    ApplicationEvents events;

    @Test
    void 신고를_처리한다() {
        // when
        ProcessReportRequest request = new ProcessReportRequest("신고 처리");

        adminReportService.process(report.getId(), request);

        // then
        assertAll(
                () -> assertThat(comment.isDeleted()).isTrue(),
                () -> assertThat(events.stream(ProcessedReportEvent.class).count()).isOne()
        );
    }

    @Test
    void 지정한_신고_식별자에_해당하는_신고가_없다면_신고_처리를_할_수_없다() {
        // given
        ProcessReportRequest request = new ProcessReportRequest("신고 처리");

        // when & then

        assertThatThrownBy(() -> adminReportService.process(-999L, request))
                .isInstanceOf(ReportNotFoundException.class)
                .hasMessage("지정한 신고 식별자로 신고 내역을 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "신고 상태가 {0}이라면 신고 처리를 할 수 없다.")
    @NullAndEmptySource
    void 지정한_신고_상태가_없다면_신고_처리를_할_수_없다(String invalidCause) {
        // given
        ProcessReportRequest request = new ProcessReportRequest(invalidCause);

        // when & then

        assertThatThrownBy(() -> adminReportService.process(report.getId(), request))
                .isInstanceOf(ReportStatusNotFoundException.class)
                .hasMessage("지정한 신고 상태를 찾을 수 없습니다.");
    }

    @Test
    void 신고_목록을_조회한다() {
        // given
        ReadAllReportSearchRequest request = new ReadAllReportSearchRequest(null, null);

        // when
        ReportCollectionResponse actual = adminReportService.findAllBy(request);

        // then
        assertAll(
                () -> assertThat(actual.reports()).hasSize(1),
                () -> assertThat(actual.lastReportId()).isEqualTo(report.getId())
        );
    }
}
