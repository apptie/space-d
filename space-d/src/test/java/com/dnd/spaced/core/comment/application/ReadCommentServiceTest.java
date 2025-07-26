package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.comment.domain.dto.LikedComment;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReadCommentServiceTest {

    private static final Long GUEST_ID = -1L;
    private static final Long READER_ID = 2L;
    private static final Long WORD_ID = 1L;

    @Autowired
    ReadCommentService readCommentService;

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql"
    })
    void 로그인_하지_않고_특정_용어의_댓글_목록을_조회한다() {
        // when
        List<LikedComment> actual = readCommentService.readComments(
                GUEST_ID,
                WORD_ID,
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).comment().getContent()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.get(0).isLiked()).isFalse()
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
        List<LikedComment> actual = readCommentService.readComments(
                READER_ID,
                WORD_ID,
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).comment().getContent()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.get(0).isLiked()).isTrue()
        );
    }
}
