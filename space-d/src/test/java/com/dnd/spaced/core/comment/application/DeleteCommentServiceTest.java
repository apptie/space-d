package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteCommentServiceTest {

    private static final Long WRITER_ID = 1L;
    private static final Long READER_ID = 2L;
    private static final Long COMMENT_ID = 1L;
    private static final Long NOT_FOUND_COMMENT_ID = -999L;

    @Autowired
    DeleteCommentService commentService;

    @Test
    void 없는_댓글_ID를_통해_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(WRITER_ID, NOT_FOUND_COMMENT_ID))
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
        assertThatThrownBy(() -> commentService.deleteComment(READER_ID, COMMENT_ID))
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
        assertDoesNotThrow(() -> commentService.deleteComment(WRITER_ID, COMMENT_ID));
    }
}
