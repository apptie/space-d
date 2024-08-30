package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void 생성자는_유효한_nickname을_전달하면_NicknameMetadata를_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new NicknameMetadata("abcde"));
    }

    @ParameterizedTest
    @MethodSource("constructorTestWithInvalidNickname")
    void 생성자는_유효하지_않은_nickname을_전달하면_InvalidNicknameMetadataException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new NicknameMetadata("aaaaaaaa"))
                .isInstanceOf(InvalidNicknameMetadataException.class)
                .hasMessage("닉네임은 최소 5글자 이상, 최대 6글자 이하여야 합니다.");
    }

    @Test
    void addCount_메서드는_nickname_개수를_1_증가시킨다() {
        // given
        NicknameMetadata nicknameMetadata = new NicknameMetadata("abcde");
        long beforeCount = nicknameMetadata.getCount();

        // when
        nicknameMetadata.addCount();

        // then
        assertThat(nicknameMetadata.getCount()).isEqualTo(beforeCount + 1);
    }

    @Test
    void getId_메서드는_NicknameMetadata의_id_역할을_하는_nickname을_반환한다() {
        // given
        NicknameMetadata nicknameMetadata = new NicknameMetadata("abcde");

        // when
        String actual = nicknameMetadata.getId();

        // then
        assertThat(actual).isEqualTo(nicknameMetadata.getNickname());
    }

    @Test
    void isNew_메서드는_NicknameMetadat가_영속화가_필요한지_여부를_반환한다() {
        // given
        NicknameMetadata nicknameMetadata = new NicknameMetadata("abcde");

        // when
        boolean actual = nicknameMetadata.isNew();

        // then
        assertThat(actual).isTrue();
    }

    private static Stream<Arguments> constructorTestWithInvalidNickname() {
        return Stream.of(
                Arguments.of((Object) null), Arguments.of(""), Arguments.of("  "),
                Arguments.of("aaaa"), Arguments.of("aaaaaaaaaaa")
        );
    }
}
