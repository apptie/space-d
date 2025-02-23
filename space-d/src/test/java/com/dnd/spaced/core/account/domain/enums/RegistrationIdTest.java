package com.dnd.spaced.core.account.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.enums.exception.InvalidRegistrationIdException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RegistrationIdTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 지원하지_않는_소셜_로그인_방식인지_확인한다(String invalidName) {
        // when
        boolean actual = RegistrationId.supports(invalidName);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 지원하는_소셜_로그인_방식인지_확인한다() {
        // when
        boolean actual = RegistrationId.supports("kakao");

        // then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 지원하지_않는_소셜_로그인_방식_이름을_전달하면_소셜_로그인_방식을_조회할_수_없다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> RegistrationId.findBy(invalidName))
                .isInstanceOf(InvalidRegistrationIdException.class)
                .hasMessageContaining("잘못된 registration id");
    }

    @Test
    void 지원하는_소셜_로그인_방식_이름을_전달하면_이름에_맞는_소셜_로그인_방식을_조회한다() {
        // when
        RegistrationId actual = RegistrationId.findBy("kakao");

        // then
        assertThat(actual).isEqualTo(RegistrationId.KAKAO);
    }
}