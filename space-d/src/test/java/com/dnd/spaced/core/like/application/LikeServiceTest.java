package com.dnd.spaced.core.like.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.comment.application.event.dto.LikedEvent;
import com.dnd.spaced.core.comment.application.event.dto.UnlikedEvent;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.application.exception.ForbiddenLikeException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @Autowired
    ApplicationEvents events;

    @Test
    @Sql(value = {
            "classpath:sql/like/account.sql",
            "classpath:sql/like/comment.sql"
    })
    void 좋아요를_추가한다() {
        // when
        likeService.processLike(1L, 1L);

        // then
        assertThat(events.stream(LikedEvent.class).count()).isOne();
    }

    @Test
    @Sql(value = {
            "classpath:sql/like/account.sql",
            "classpath:sql/like/comment.sql",
            "classpath:sql/like/like.sql"
    })
    void 좋아요를_취소한다() {
        // when
        likeService.processLike(1L, 1L);

        // then
        assertThat(events.stream(UnlikedEvent.class).count()).isOne();
    }

    @Test
    @Sql("classpath:sql/like/account.sql")
    void 존재하지_않는_댓글_ID로_좋아요를_할_수_없다() {
        // when & then
                assertThatThrownBy(() -> likeService.processLike(1L, -999L))
                .isInstanceOf(AssociationCommentNotFoundException.class)
                .hasMessage("좋아요 대상인 댓글을 찾을 수 없습니다.");
    }

    @Test
    @Sql("classpath:sql/like/comment.sql")
    void 없거나_이미_탈퇴한_회원_ID로_좋아요를_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> likeService.processLike(-999L, 1L))
                .isInstanceOf(ForbiddenLikeException.class)
                .hasMessage("지정한 ID에 대한 회원을 찾지 못했습니다.");
    }
}
