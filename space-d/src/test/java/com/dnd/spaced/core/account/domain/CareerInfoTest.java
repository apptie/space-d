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
    void build_메서드는_유효한_experienceName_companyName_jobGroupName을_전달하면_CareerInfo를_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(
                () -> CareerInfo.builder()
                                .experienceName(Experience.BETWEEN_FIRST_SECOND.getName())
                                .companyName(Company.BLIND.getName())
                                .jobGroupName(JobGroup.DEVELOP.getName())
                                .build()
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_experienceName을_전달하면_InvalidExperienceException_예외가_발생한다(String invalidExperienceName) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_companyName을_전달하면_InvalidCompanyException_예외가_발생한다(String invalidCompanyName) {
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

    @ParameterizedTest
    @NullAndEmptySource
    void build_메서드는_유효하지_않은_jobGroupName을_전달하면_InvalidJobGroupException_예외가_발생한다(String invalidJobGroupName) {
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
