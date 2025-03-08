package com.dnd.spaced.core.report.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReportReasonTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("광고 및 홍보성 내용", ReportReason.SPAM),
                Arguments.of("댓글 도배", ReportReason.OVER_COMMENT),
                Arguments.of("욕설, 음란 등 부적절한 내용", ReportReason.PROFANITY),
                Arguments.of("기타", ReportReason.ETC)
        );
    }

    @ParameterizedTest(name = "원인이 {0} 일 때 {1}을 찾는다")
    @MethodSource("findByTestArguments")
    void 신고_사유를_원인을_통해_조회한다(String cause, ReportReason expected) {
        // when
        Optional<ReportReason> actual = ReportReason.findBy(cause);

        // then
        assertThat(actual).contains(expected);
    }

    @ParameterizedTest(name = "원인이 {0}일 때 신고 사유를 찾지 못한다")
    @NullAndEmptySource
    void 신고_사유에_없는_원인이라면_신고_사유를_찾을_수_없다(String invalidCause) {
        // when
        Optional<ReportReason> actual = ReportReason.findBy(invalidCause);

        // then
        assertThat(actual).isNotPresent();
    }
}
