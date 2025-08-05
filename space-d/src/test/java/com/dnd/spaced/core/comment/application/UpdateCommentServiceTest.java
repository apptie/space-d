package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateCommentServiceTest {

    private static final Long WRITER_ID = 1L;
    private static final Long READER_ID = 2L;
    private static final Long COMMENT_ID = 1L;
    private static final Long NOT_FOUND_COMMENT_ID = -999L;

    @Autowired
    UpdateCommentService updateCommentService;

    @Test
    void 댓글_ID로_찾을_수_없는_댓글은_수정할_수_없다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() -> updateCommentService.updateComment(WRITER_ID, NOT_FOUND_COMMENT_ID, request))
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
        assertThatThrownBy(() -> updateCommentService.updateComment(READER_ID, COMMENT_ID, request))
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
        assertThatThrownBy(() -> updateCommentService.updateComment(WRITER_ID, COMMENT_ID, request))
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
        assertDoesNotThrow(() -> updateCommentService.updateComment(WRITER_ID, COMMENT_ID, request));
    }
}
