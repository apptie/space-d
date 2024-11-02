package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidExperienceException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExperienceTest {

    @Test
    void 경력과_일치하는_도메인을_반환한다() {
        // given
        Experience betweenFirstSecond = Experience.BETWEEN_FIRST_SECOND;

        // when
        Experience actual = Experience.findBy(betweenFirstSecond.getName());

        // then
        assertThat(actual).isEqualTo(betweenFirstSecond);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유효한_경력이_아닌_경우_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> Experience.findBy(invalidName))
                .isInstanceOf(InvalidExperienceException.class)
                .hasMessageContaining("잘못된 경력");
    }
}
