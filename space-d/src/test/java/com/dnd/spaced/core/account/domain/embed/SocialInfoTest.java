package com.dnd.spaced.core.account.domain.embed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SocialInfoTest {

    @Test
    void 회원의_소셜_정보를_초기화한다() {
        // given
        RegistrationId registrationId = RegistrationId.findBy("kakao");

        // when
        SocialInfo socialInfo = new SocialInfo(registrationId, "41258");

        // then
        assertAll(
                () -> assertThat(socialInfo.getSocialIdentifier()).isEqualTo("41258"),
                () -> assertThat(socialInfo.getRegistrationId()).isEqualTo(registrationId)
        );
    }
}