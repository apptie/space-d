package com.dnd.spaced.core.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentTest {

    @Test
    void 댓글을_초기화한다() {
        // when & then
        Comment actual = assertDoesNotThrow(
                () -> new Comment(1L, 1L, "이 용어 언제 쓰는건가요?")
        );

        assertAll(
                () -> assertThat(actual.getAccountId()).isEqualTo(1L),
                () -> assertThat(actual.getWordId()).isEqualTo(1L),
                () -> assertThat(actual.getContent()).isEqualTo("이 용어 언제 쓰는건가요?")
        );
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 댓글을 초기화할 수 없다")
    @NullAndEmptySource
    void 비어있는_내용으로_댓글을_초기화할_수_없다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> new Comment(1L, 1L, invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 지정한_회원이_댓글의_작성자가_아닌지_확인한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();
        ReflectionTestUtils.setField(account, "id", 1L);
        Comment comment = new Comment(2L, 1L, "이 용어 언제 쓰는건가요?");

        // when
        boolean actual = comment.isNotWriter(account);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지정한_회원이_댓글의_작성자인지_확인한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        ReflectionTestUtils.setField(writer, "id", 1L);
        Comment comment = new Comment(writer.getId(), 1L, "이 용어 언제 쓰는건가요?");

        // when
        boolean actual = comment.isNotWriter(writer);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 회원_ID로_댓글의_작성자인지_확인한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        ReflectionTestUtils.setField(writer, "id", 1L);
        Comment comment = new Comment(writer.getId(), 1L, "이 용어 언제 쓰는건가요?");

        // when
        boolean actual = comment.isWriter(1L);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 댓글을_수정한다() {
        // given
        Comment comment = new Comment(1L, 1L, "이 용어 언제 쓰는건가요?");

        // when
        String changedContent = "이 용어 쓰기는 하는건가요? 쓰는 꼴을 못 본거 같은데";

        comment.changeContent(changedContent);

        // when & then
        assertThat(comment.getContent()).isEqualTo(changedContent);

    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 댓글을 수정할 수 없다")
    @NullAndEmptySource
    void 유효한_길이가_아닌_내용으로_댓글을_수정할_수_없다(String invalidContent) {
        // given
        Comment comment = new Comment(1L, 1L, "이 용어 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> comment.changeContent(invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글을_삭제한다() {
        // given
        Comment comment = new Comment(1L, 1L, "이 용어 언제 쓰는건가요?");

        // when
        comment.delete();

        // then
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test
    void 삭제한_댓글을_복구한다() {
        // given
        Comment comment = new Comment(1L, 1L, "이 용어 언제 쓰는건가요?");
        comment.delete();

        // when
        comment.recover();

        // then
        assertThat(comment.isDeleted()).isFalse();
    }
}
