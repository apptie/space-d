package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CompanyTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("대기업", Company.MAJOR),
                Arguments.of("중견기업", Company.MIDSIZE),
                Arguments.of("중소기업", Company.SMALL),
                Arguments.of("스타트업", Company.STARTUP),
                Arguments.of("외국계", Company.FOREIGN),
                Arguments.of("취준생/인턴", Company.JOB_HUNTER_INTERN),
                Arguments.of("비공개", Company.BLIND)
        );
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByTestArguments")
    void 회사_정보를_찾는다(String companyName, Company expected) {
        // when
        Company actual = Company.findBy(companyName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 회사_정보를_찾을_때_유효한_회사명이_아닌_경우_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> Company.findBy(invalidName))
                .isInstanceOf(InvalidCompanyException.class)
                .hasMessageContaining("잘못된 회사 이름");
    }
}
