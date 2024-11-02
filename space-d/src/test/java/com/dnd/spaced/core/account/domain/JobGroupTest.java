package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JobGroupTest {

    @Test
    void 직군과_일치하는_도메인을_반환한다() {
        // given
        JobGroup develop = JobGroup.DEVELOP;

        // when
        JobGroup actual = JobGroup.findBy(develop.getName());

        // then
        assertThat(actual).isEqualTo(develop);
    }

    @ParameterizedTest(name = "직군이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 유효한_직군이_아닌_경우_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> JobGroup.findBy(invalidName))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }
}
