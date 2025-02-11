package com.dnd.spaced.core.like.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.config.clean.annotation.CleanUpPersistence;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.application.exception.ForbiddenLikeException;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@CleanUpPersistence
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeRepository likeRepository;

    @Test
    void 좋아요를_누른_회원이_아닌_다른_회원은_좋아요를_취소할_수_없다() {
        // when & then
        assertThatThrownBy(() -> likeService.processLike("accountId", -1L))
                .isInstanceOf(ForbiddenLikeException.class)
                .hasMessage("좋아요를 제어할 권한이 없습니다.");
    }

    @Test
    void 존재하지_않는_댓글에_좋아요를_할_수_없다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(() -> likeService.processLike(account.getId(), -1L))
                .isInstanceOf(AssociationCommentNotFoundException.class)
                .hasMessage("좋아요 대상인 댓글을 찾을 수 없습니다.");
    }

    @Test
    void 좋아요를_취소한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        accountRepository.save(account);
        wordRepository.save(word);

        Comment comment = new Comment(account.getId(), word.getId(), "댓글");

        commentRepository.save(comment);
        likeService.processLike(account.getId(), comment.getId());

        // when
        likeService.processLike(account.getId(), comment.getId());

        // then
        Optional<Like> actual = likeRepository.findBy(account.getId(), comment.getId());

        assertThat(actual).isEmpty();
    }

    @Test
    void 좋아요를_추가한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();

        accountRepository.save(account);
        wordRepository.save(word);

        Comment comment = new Comment(account.getId(), word.getId(), "댓글");

        commentRepository.save(comment);

        // when
        likeService.processLike(account.getId(), comment.getId());

        // then
        Optional<Like> actual = likeRepository.findBy(account.getId(), comment.getId());

        assertThat(actual).isPresent();
    }
}
