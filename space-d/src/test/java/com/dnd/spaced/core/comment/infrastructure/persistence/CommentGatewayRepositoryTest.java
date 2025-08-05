package com.dnd.spaced.core.comment.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedComment;
import jakarta.persistence.EntityManager;
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

    private static final Long WRITER_ID = 1L;
    private static final Long READER_ID = 2L;
    private static final Long GUEST_ID = -1L;
    private static final Long WORD_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final Long DELETED_COMMENT_ID = 2L;

    @Autowired
    CommentGatewayRepository commentGatewayRepository;

    @Autowired
    CommentCrudRepository commentCrudRepository;

    @Autowired
    EntityManager em;

    @Test
    void 댓글을_영속화_한다() {
        // given
        Comment comment = new Comment(WRITER_ID, WORD_ID, "이 용어 언제 쓰는건가요?");

        // when
        Comment actual = commentGatewayRepository.save(comment);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 삭제하지_않은_댓글을_id로_조회한다() {
        // when
        Optional<Comment> actual = commentGatewayRepository.findBy(COMMENT_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(COMMENT_ID)
        );
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    void 삭제한_댓글은_id로_조회할_수_없다() {
        // when
        Optional<Comment> actual = commentGatewayRepository.findBy(DELETED_COMMENT_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql",
    })
    void 로그인하지_않은_상태로_용어의_삭제하지_않은_모든_댓글을_조회한다() {
        // when
        List<LikedComment> actual = commentGatewayRepository.findAllBy(GUEST_ID, WORD_ID, null, PageRequest.of(0, 10));

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
            "classpath:sql/comment/like.sql",
    })
    void 로그인한_상태로_용어의_삭제하지_않은_모든_댓글을_조회한다() {
        // when
        List<LikedComment> actual = commentGatewayRepository.findAllBy(READER_ID, WORD_ID, null, PageRequest.of(0, 10));

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
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql"
    })
    @Transactional
    void 삭제하지_않은_댓글에_좋아요_카운트를_1_증가시킨다() {
        // given
        Comment comment = commentGatewayRepository.findBy(COMMENT_ID).get();

        assertThat(comment.getLikeCount()).isEqualTo(1L);

        // when
        commentGatewayRepository.addLikeCount(COMMENT_ID);

        // then
        em.clear();

        Optional<Comment> actual = commentGatewayRepository.findBy(COMMENT_ID);

        assertThat(actual.get().getLikeCount()).isEqualTo(2L);
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    @Transactional
    void 삭제한_댓글에_좋아요_카운트를_증가시킬_수_없다() {
        // given
        Comment deletedComment = commentCrudRepository.findById(DELETED_COMMENT_ID).get();

        assertThat(deletedComment.getLikeCount()).isEqualTo(1L);

        // when
        commentGatewayRepository.addLikeCount(DELETED_COMMENT_ID);

        // then
        em.clear();

        Optional<Comment> actual = commentCrudRepository.findById(DELETED_COMMENT_ID);

        assertThat(actual.get().getLikeCount()).isEqualTo(1L);
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql",
            "classpath:sql/comment/like.sql",
    })
    @Transactional
    void 삭제하지_않은_댓글에_좋아요_카운트를_1_감소시킨다() {
        // given
        Comment comment = commentGatewayRepository.findBy(COMMENT_ID).get();

        assertThat(comment.getLikeCount()).isEqualTo(1L);

        // when
        commentGatewayRepository.subtractLikeCount(COMMENT_ID);

        // then
        em.clear();

        Comment actual = commentGatewayRepository.findBy(COMMENT_ID).get();

        assertThat(actual.getLikeCount()).isZero();
    }

    @Test
    @Sql(value = {
            "classpath:sql/comment/word.sql",
            "classpath:sql/comment/comment.sql"
    })
    @Transactional
    void 삭제한_댓글에_좋아요_카운트를_감소시킬_수_없다() {
        // given
        Comment deletedComment = commentCrudRepository.findById(DELETED_COMMENT_ID).get();

        assertThat(deletedComment.getLikeCount()).isEqualTo(1L);

        // when
        commentGatewayRepository.subtractLikeCount(DELETED_COMMENT_ID);

        // then
        em.clear();

        Optional<Comment> actual = commentCrudRepository.findById(DELETED_COMMENT_ID);

        assertThat(actual.get().getLikeCount()).isEqualTo(1L);
    }
}
