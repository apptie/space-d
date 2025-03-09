package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.exception.InvalidNicknameMetadataException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NicknameMetadataTest {

    @Test
    void 닉네임_메타데이터를_초기화한다() {
        // when & then
        NicknameMetadata actual = assertDoesNotThrow(() -> NicknameMetadata.from("재빠른지구"));

        assertAll(
                () -> assertThat(actual.getNickname()).isEqualTo("재빠른지구"),
                () -> assertThat(actual.getCount()).isEqualTo(1L)
        );
    }

    private static Stream<Arguments> constructorTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("1234"),
                Arguments.of("1234567")
        );
    }

    @ParameterizedTest(name = "닉네임이 {0}일 때 닉네임 메타데이터를 초기화할 수 없다")
    @MethodSource("constructorTestWithInvalidNickname")
    void 길이가_유효하지_않은_닉네임이라면_닉네임_메타데이터를_초기화할_수_없다(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> NicknameMetadata.from(invalidNickname))
                .isInstanceOf(InvalidNicknameMetadataException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 6글자 이하여야 합니다.");
    }

    @Test
    void 닉네임_메타데이터의_닉네임_생성_횟수를_1_증가시킨다() {
        // given
        NicknameMetadata nicknameMetadata = NicknameMetadata.from("재빠른지구");
        long beforeCount = nicknameMetadata.getCount();

        // when
        nicknameMetadata.addCount();

        // then
        assertThat(nicknameMetadata.getCount()).isEqualTo(beforeCount + 1);
    }

    @Test
    void 닉네임_메타데이터_식별자를_반환한다() {
        // given
        NicknameMetadata nicknameMetadata = NicknameMetadata.from("재빠른지구");

        // when
        String actual = nicknameMetadata.getId();

        // then
        assertThat(actual).isEqualTo(nicknameMetadata.getNickname());
    }

    @Test
    void 닉네임_메타데이터의_영속화_여부를_반환한다() {
        // given
        NicknameMetadata nicknameMetadata = NicknameMetadata.from("재빠른지구");

        // when
        boolean actual = nicknameMetadata.isNew();

        // then
        assertThat(actual).isTrue();
    }
}
