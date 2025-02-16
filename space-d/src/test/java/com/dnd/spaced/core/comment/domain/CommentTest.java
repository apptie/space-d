package com.dnd.spaced.core.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentTest {

    @Test
    void 댓글을_초기화한다() {
        // when & then
        assertDoesNotThrow(() -> new Comment("user1@naver.com", 1L, "이 용어 언제 쓰는건가요?"));
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 비어있는_내용으로_댓글을_초기화할_수_없다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> new Comment("user1@naver.com", 1L, invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글의_작성자가_아닌지_확인한다() {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();
        Comment comment = new Comment("user2@naver.com", 1L, "이 용어 언제 쓰는건가요?");

        // when
        boolean actual = comment.isNotWriter(account);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 댓글의_작성자인지_확인한다() {
        // given
        Account account = Account.builder()
                                 .id("user1@naver.com")
                                 .nickname("재빠른지구")
                                 .profileImage("earth.png")
                                 .roleName("ROLE_USER")
                                 .build();
        Comment comment = new Comment(account.getId(), 1L, "이 용어 언제 쓰는건가요?");

        // when
        boolean actual = comment.isNotWriter(account);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 댓글을_수정한다() {
        // given
        Comment comment = new Comment("user1@naver.com", 1L, "이 용어 언제 쓰는건가요?");

        // when
        String changedContent = "이 용어 쓰기는 하는건가요? 쓰는 꼴을 못 본거 같은데";

        comment.changeContent(changedContent);

        // when & then
        assertThat(comment.getContent()).isEqualTo(changedContent);

    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 비어있는_내용으로_댓글을_수정할_수_없다(String invalidContent) {
        // given
        Comment comment = new Comment("user1@naver.com", 1L, "이 용어 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> comment.changeContent(invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }
}
