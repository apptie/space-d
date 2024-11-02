package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JobGroupTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("개발자", JobGroup.DEVELOP),
                Arguments.of("디자이너", JobGroup.DESIGN),
                Arguments.of("기타", JobGroup.ETC)
        );
    }

    @ParameterizedTest(name = "직군이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByTestArguments")
    void 직군_정보를_찾는다(String jobGroupName, JobGroup expected) {
        // when
        JobGroup actual = JobGroup.findBy(jobGroupName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "직군이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 직군_정보를_찾을_때_유효한_직군이_아닌_경우_직군_정보를_찾을_수_없다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> JobGroup.findBy(invalidName))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }
}
