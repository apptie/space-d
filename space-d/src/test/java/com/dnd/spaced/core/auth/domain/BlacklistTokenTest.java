package com.dnd.spaced.core.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.auth.domain.exception.InvalidBlacklistTokenContentException;
import com.dnd.spaced.fixture.LocalDateTimeFixture;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BlacklistTokenTest {

    @ParameterizedTest(name = "registeredAt이 {0}이며, issuedAt이 {1}일 때 {2}를 반환한다")
    @MethodSource("isBlacklistTokenTestWithRegisteredAtAndIssuedAtAndExpected")
    void isBlacklistToken_메서드는_issuedAt을_전달하면_블랙리스트된_토큰인지_여부를_반환한다(String targetRegisteredAt, String targetIssuedAt, boolean expected) {
        // given
        String email = "email";
        LocalDateTime registeredAt = LocalDateTimeFixture.from(targetRegisteredAt);
        LocalDateTime issuedAt = LocalDateTimeFixture.from(targetIssuedAt);

        BlacklistToken blacklistToken = new BlacklistToken(email, registeredAt);

        // when
        boolean actual = blacklistToken.isBlacklistToken(issuedAt);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "email이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 생성자는_유효하지_않은_email을_전달하면_InvalidBlacklistTokenContentException_예외가_발생한다(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> new BlacklistToken(invalidEmail, LocalDateTime.now()))
                .isInstanceOf(InvalidBlacklistTokenContentException.class)
                .hasMessage("유효한 이메일이 아닙니다.");
    }

    @Test
    void 생성자는_유효하지_않은_registeredAt을_전달하면_InvalidBlacklistTokenContentException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new BlacklistToken("email", null))
                .isInstanceOf(InvalidBlacklistTokenContentException.class)
                .hasMessage("유효한 등록 일자가 아닙니다.");
    }

    private static Stream<Arguments> isBlacklistTokenTestWithRegisteredAtAndIssuedAtAndExpected() {
        return Stream.of(
                Arguments.of("2024-03-30 11:00:00", "2024-03-30 12:00:00", false),
                Arguments.of("2024-03-30 11:00:00", "2024-03-30 10:00:00", true)
        );
    }
}
