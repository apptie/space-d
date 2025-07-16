package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.application.exception.WordNotFoundException;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    void 댓글을_작성할_용어가_없는_경우_댓글을_작성할_수_없다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.createComment(1L, -1L, request))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("댓글과 관련된 용어를 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 댓글을 작성할 수 없다")
    @NullAndEmptySource
    @Sql("classpath:sql/comment/word.sql")
    void 유효한_길이의_댓글_내용이_아니라면_댓글을_작성할_수_없다(String invalidContent) {
        // given
        CreateCommentRequest request = new CreateCommentRequest(invalidContent);

        // when & then
        assertThatThrownBy(() -> commentService.createComment(1L, 1L, request))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    @Sql("classpath:sql/comment/word.sql")
    void 댓글을_작성한다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when
        assertDoesNotThrow(() -> commentService.createComment(1L, 1L, request));
    }

    @Test
    void 없는_댓글_식별자를_통해_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(1L, -999L))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 댓글_작성자가_아니라면_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(2L, 1L))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 삭제할 권한이 없습니다.");
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 댓글을_삭제한다() {
        // when & then
        assertDoesNotThrow(() -> commentService.deleteComment(1L, 1L));
    }

    @Test
    void 식별할_수_없는_댓글_식별자로_댓글을_수정할_수_없다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(1L, -999L, request))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 댓글_작성자가_아니라면_댓글을_수정할_수_없다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(2L, 1L, request))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 수정할 권한이 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 비어_있는_내용으로_댓글을_수정할_수_없다(String invalidContent) {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest(invalidContent);

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(1L, 1L, request))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 댓글을_수정한다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertDoesNotThrow(() -> commentService.updateComment(1L, 1L, request));
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql"
    })
    void 로그인_하지_않고_특정_용어의_댓글_목록을_조회한다() {
        // when
        CommentCollectionResponse actual = commentService.readComments(
                -1L,
                1L,
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.comments()).hasSize(1),
                () -> assertThat(actual.comments().get(0).commentContent().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.comments().get(0).liked()).isFalse()
        );
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql"
    })
    void 로그인하고_특정_용어의_댓글_목록을_조회한다() {
        // when
        CommentCollectionResponse actual = commentService.readComments(
                2L,
                1L,
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.comments()).hasSize(1),
                () -> assertThat(actual.comments().get(0).commentContent().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.comments().get(0).liked()).isTrue()
        );
    }
}
