package com.dnd.spaced.core.report.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReportStatusTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("처리 전", ReportStatus.PENDING),
                Arguments.of("신고 처리", ReportStatus.PROCESSED),
                Arguments.of("신고 반려", ReportStatus.UN_PROCESSED)
        );
    }

    @ParameterizedTest(name  = "상태 이름이 {0}일 때 {1}을 찾는다")
    @MethodSource("findByTestArguments")
    void 상태_이름으로_신고_상태를_찾는다(String name, ReportStatus expected) {
        // when
        Optional<ReportStatus> actual = ReportStatus.findBy(name);

        // then
        assertThat(actual).contains(expected);
    }

    @ParameterizedTest(name = "상태 이름이 {0}일 때 신고 상태를 찾을 수 없다")
    @NullAndEmptySource
    void 유효하지_않은_상태_이름으로_조회하면_신고_상태를_찾을_수_없다(String invalidName) {
        // when
        Optional<ReportStatus> actual = ReportStatus.findBy(invalidName);

        // then
        assertThat(actual).isNotPresent();
    }

    @Test
    void 신고_상태가_신고_처리_상태인지_확인한다() {
        // given
        ReportStatus reportStatus = ReportStatus.PROCESSED;

        // when
        boolean actual = reportStatus.isProcess();

        // then
        assertThat(actual).isTrue();
    }
}
