package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExperienceTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("1년 차 미만", Experience.UNDER_FIRST),
                Arguments.of("1~2년 차", Experience.BETWEEN_FIRST_SECOND),
                Arguments.of("2~3년 차", Experience.BETWEEN_SECOND_THIRD),
                Arguments.of("3~4년 차", Experience.BETWEEN_THIRD_FOURTH),
                Arguments.of("4~5년 차", Experience.BETWEEN_FOURTH_FIFTH),
                Arguments.of("5년 차 이상", Experience.OVER_FIFTH),
                Arguments.of("비공개", Experience.BLIND)
        );
    }

    @ParameterizedTest(name = "경력이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByTestArguments")
    void 경력_정보를_찾는다(String experienceName, Experience expected) {
        // when
        Experience actual = Experience.findBy(experienceName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보를_찾을_때_유효한_경력이_아닌_경우_경력_정보를_찾을_수_없다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> Experience.findBy(invalidName))
                .isInstanceOf(InvalidExperienceException.class)
                .hasMessageContaining("잘못된 경력");
    }
}
