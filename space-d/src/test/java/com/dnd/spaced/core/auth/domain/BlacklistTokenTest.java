package com.dnd.spaced.core.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    private static Stream<Arguments> isBlacklistTokenTestWithRegisteredAtAndIssuedAtAndExpected() {
        return Stream.of(
                Arguments.of("2024-03-30 11:00:00", "2024-03-30 12:00:00", false),
                Arguments.of("2024-03-30 11:00:00", "2024-03-30 10:00:00", true)
        );
    }

    @ParameterizedTest(name = "registeredAt이 {0}이며, issuedAt이 {1}일 때 {2}를 반환한다")
    @MethodSource("isBlacklistTokenTestWithRegisteredAtAndIssuedAtAndExpected")
    void 등록_일자를_통해_블랙리스트에_등록된_토큰인지_판단한다(String targetRegisteredAt, String targetIssuedAt, boolean expected) {
        // given
        String id = "user1@naver.com";
        LocalDateTime registeredAt = LocalDateTimeFixture.from(targetRegisteredAt);
        LocalDateTime issuedAt = LocalDateTimeFixture.from(targetIssuedAt);

        BlacklistToken blacklistToken = new BlacklistToken(id, registeredAt);

        // when
        boolean actual = blacklistToken.isBlacklistToken(issuedAt);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 블랙리스트_토큰에_등록될_회원_식별자와_등록_일자로_블랙리스트_토큰을_초기화한다() {
        // when & then
        assertDoesNotThrow(() -> new BlacklistToken("user1@naver.com", LocalDateTime.now()));
    }

    @ParameterizedTest(name = "id가 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 블랙리스트_토큰_초기화_시_비어_있는_식별자라면_블랙리스트_토큰을_초기화_할_수_없다(String invalidId) {
        // when & then
        assertThatThrownBy(() -> new BlacklistToken(invalidId, LocalDateTime.now()))
                .isInstanceOf(InvalidBlacklistTokenContentException.class)
                .hasMessage("유효한 ID가 아닙니다.");
    }

    @Test
    void 블랙리스트_토큰_초기화_시_유효한_블랙리스트_등록_일자가_아니라면_초기화_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> new BlacklistToken("user1@naver.com", null))
                .isInstanceOf(InvalidBlacklistTokenContentException.class)
                .hasMessage("유효한 등록 일자가 아닙니다.");
    }
}
