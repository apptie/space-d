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
    void findBy_메서드는_유효한_name을_전달하면_name에_맞는_JobGroup을_반환한다() {
        // given
        JobGroup develop = JobGroup.DEVELOP;

        // when
        JobGroup actual = JobGroup.findBy(develop.getName());

        // then
        assertThat(actual).isEqualTo(develop);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_name을_전달하면_InvalidJobGroupException_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> JobGroup.findBy(invalidName))
                .isInstanceOf(InvalidJobGroupException.class)
                .hasMessageContaining("잘못된 직군 이름");
    }
}
