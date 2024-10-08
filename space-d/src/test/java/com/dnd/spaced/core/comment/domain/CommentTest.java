package com.dnd.spaced.core.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Role;
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
    void 생성자는_유효한_accountId_wordId_content를_전달하면_Comment를_초기화하고_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new Comment("accountId", 1L, "댓글"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 생성자는_유효하지_않은_content를_전달하면_InvalidCommentContentException_예외가_발생한다(String invalidContent) {
        // when & then
        assertThatThrownBy(() -> new Comment("accountId", 1L, invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void isNotOnwer_메서드는_accountId를_전달하면_해당_Comment의_작성자인지_여부를_반환한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();
        Comment comment = new Comment(account.getId(), 1L, "댓글");

        // when
        boolean actual = comment.isNotWriter(account);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void changeContent_메서드는_유효한_accountId_wordId_content를_전달하면_Comment를_초기화하고_반환한다() {
        // given
        Comment comment = new Comment("accountId", 1L, "댓글");

        // when
        String changedContent = "변경";

        comment.changeContent(changedContent);

        // when & then
        assertThat(comment.getContent()).isEqualTo(changedContent);

    }

    @ParameterizedTest
    @NullAndEmptySource
    void changeContent_메서드는_유효하지_않은_content를_전달하면_InvalidCommentContentException_예외가_발생한다(String invalidContent) {
        // given
        Comment comment = new Comment("accountId", 1L, "댓글");

        // when & then
        assertThatThrownBy(() -> comment.changeContent(invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }
}
