package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
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
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCommentServiceTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final Long WORD_ID = 1L;
    private static final Long NOT_FOUND_WORD_ID = -1L;

    @Autowired
    CreateCommentService createCommentService;

    @Test
    void 댓글을_작성할_용어_ID로_용어를_찾지_못하는_경우_댓글을_작성할_수_없다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> createCommentService.createComment(ACCOUNT_ID, NOT_FOUND_WORD_ID, request))
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
        assertThatThrownBy(() -> createCommentService.createComment(ACCOUNT_ID, WORD_ID, request))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    @Sql("classpath:sql/comment/word.sql")
    void 댓글을_작성한다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when
        assertDoesNotThrow(() -> createCommentService.createComment(ACCOUNT_ID, WORD_ID, request));
    }
}
