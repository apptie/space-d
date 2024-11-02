package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CareerInfoTest {

    @Test
    void 경력_정보를_초기화한다() {
        // when & then
        assertDoesNotThrow(
                () -> CareerInfo.builder()
                                .experienceName(Experience.BETWEEN_FIRST_SECOND.getName())
                                .companyName(Company.BLIND.getName())
                                .jobGroupName(JobGroup.DEVELOP.getName())
                                .build()
        );
    }

    @ParameterizedTest(name = "경력이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보를_초기화할_때_유효한_경력이_아니라면_예외가_발생한다(String invalidExperienceName) {
        // when & then
        assertThatThrownBy(
                () -> CareerInfo.builder()
                                .experienceName(invalidExperienceName)
                                .companyName(Company.BLIND.getName())
                                .jobGroupName(JobGroup.DEVELOP.getName())
                                .build()
        ).isInstanceOf(InvalidExperienceException.class)
         .hasMessageContaining("잘못된 경력");
    }

    @ParameterizedTest(name = "회사명이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보를_초기화할_때_유효한_회사명이_아니라면_예외가_발생한다(String invalidCompanyName) {
        // when & then
        assertThatThrownBy(
                () -> CareerInfo.builder()
                                .experienceName(Experience.BETWEEN_FIRST_SECOND.getName())
                                .companyName(invalidCompanyName)
                                .jobGroupName(JobGroup.DEVELOP.getName())
                                .build()
        ).isInstanceOf(InvalidCompanyException.class)
         .hasMessageContaining("잘못된 회사 이름");
    }

    @ParameterizedTest(name = "직군 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 경력_정보를_초기화할_때_유효한_직군_이름이_아니라면_예외가_발생한다(String invalidJobGroupName) {
        // when & then
        assertThatThrownBy(
                () -> CareerInfo.builder()
                                .experienceName(Experience.BETWEEN_FIRST_SECOND.getName())
                                .companyName(Company.BLIND.getName())
                                .jobGroupName(invalidJobGroupName)
                                .build()
        ).isInstanceOf(InvalidJobGroupException.class)
         .hasMessageContaining("잘못된 직군 이름");
    }
}
