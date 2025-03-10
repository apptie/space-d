package com.dnd.spaced.core.like.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.comment.application.event.dto.LikedEvent;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.application.exception.ForbiddenLikeException;
import com.dnd.spaced.core.like.application.helper.WithAccountAndCommentTestHelper;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@RecordApplicationEvents
@CleanUpDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeServiceTest extends WithAccountAndCommentTestHelper {

    @Autowired
    LikeService likeService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ApplicationEvents events;

    @Test
    void 좋아요를_추가한다() {
        // when
        likeService.processLike(account.getId(), comment.getId());

        // then
        Optional<Like> actual = likeRepository.findBy(account.getId(), comment.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(events.stream(LikedEvent.class).count()).isOne()
        );
    }

    @Test
    void 좋아요를_취소한다() {
        // given
        likeService.processLike(account.getId(), comment.getId());

        // when
        likeService.processLike(account.getId(), comment.getId());

        // then
        Optional<Like> actual = likeRepository.findBy(account.getId(), comment.getId());

        assertAll(
                () -> assertThat(actual).isEmpty(),
                () -> assertThat(events.stream(LikedEvent.class).count()).isOne()
        );
    }

    @Test
    void 존재하지_않는_댓글_ID로_좋아요를_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> likeService.processLike(account.getId(), -1L))
                .isInstanceOf(AssociationCommentNotFoundException.class)
                .hasMessage("좋아요 대상인 댓글을 찾을 수 없습니다.");
    }

    @Test
    void 없거나_이미_탈퇴한_회원_ID로_좋아요를_할_수_없다() {
        // when & then
        assertThatThrownBy(() -> likeService.processLike(-999L, comment.getId()))
                .isInstanceOf(ForbiddenLikeException.class)
                .hasMessage("좋아요를 제어할 권한이 없습니다.");
    }
}
