package com.dnd.spaced.core.comment.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedCommentInfo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentGatewayRepositoryTest {

    @Autowired
    CommentGatewayRepository commentRepository;

    @Autowired
    CommentCrudRepository commentCrudRepository;

    @Test
    void 댓글을_영속화_한다() {
        // given
        Comment comment = new Comment(1L, 1L, "이 용어 언제 쓰는건가요?");

        // when
        Comment actual = commentRepository.save(comment);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 삭제하지_않은_댓글을_댓글_식별자로_조회한다() {
        // when
        Optional<Comment> actual = commentRepository.findBy(1L);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/deleted_comment.sql"
    })
    void 삭제한_댓글은_댓글_식별자로_조회할_수_없다() {
        // when
        Optional<Comment> actual = commentRepository.findBy(2L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/deleted_comment.sql",
            "classpath:sql/comment/like.sql",
    })
    void 로그인하지_않은_상태로_용어의_삭제하지_않은_모든_댓글을_조회한다() {
        // when
        List<LikedCommentInfo> actual = commentRepository.findAllBy(-1L, 1L, null, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).comment().getId()).isEqualTo(1L),
                () -> assertThat(actual.get(0).isLiked()).isFalse()
        );
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/deleted_comment.sql",
            "classpath:sql/comment/like.sql",
    })
    void 로그인한_상태로_용어의_삭제하지_않은_모든_댓글을_조회한다() {
        // when
        List<LikedCommentInfo> actual = commentRepository.findAllBy(2L, 1L, null, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).comment().getId()).isEqualTo(1L),
                () -> assertThat(actual.get(0).isLiked()).isTrue()
        );
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    @Transactional
    void 삭제하지_않은_댓글에_좋아요_카운트를_1_증가시킨다() {
        // when
        commentRepository.increaseLikeCount(1L);

        // then
        Optional<Comment> actual = commentRepository.findBy(1L);

        assertThat(actual.get().getLikeCount()).isEqualTo(1L);
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/deleted_comment.sql"
    })
    @Transactional
    void 삭제한_댓글에_좋아요_카운트를_증가시킬_수_없다() {
        // when
        commentRepository.increaseLikeCount(2L);

        // then
        Optional<Comment> actual = commentCrudRepository.findById(2L);

        assertThat(actual.get().getLikeCount()).isZero();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql",
    })
    @Transactional
    void 삭제하지_않은_댓글에_좋아요_카운트를_1_감소시킨다() {
        // when
        commentRepository.decreaseLikeCount(1L);

        // then
        Optional<Comment> actual = commentRepository.findBy(1L);

        assertThat(actual.get().getLikeCount()).isZero();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/deleted_comment.sql"
    })
    @Transactional
    void 삭제한_댓글에_좋아요_카운트를_감소시킬_수_없다() {
        // when
        commentRepository.decreaseLikeCount(2L);

        // then
        Optional<Comment> actual = commentCrudRepository.findById(2L);

        assertThat(actual.get().getLikeCount()).isZero();
    }
}
