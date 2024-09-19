package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.application.exception.AssociationAccountNotFoundException;
import com.dnd.spaced.core.comment.application.exception.AssociationWordNotFoundException;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import com.dnd.spaced.core.like.infrastructure.LikeCountRedisRepository;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@CleanUpDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    LikeCountRedisRepository likeCountRedisRepository;

    @Test
    void save_메서드는_없는_회원을_전달하면_AssociationAccountNotFoundException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> commentService.save("accountId", -1L, "댓글"))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void save_메서드는_지정한_용어_ID가_없다면_AssociationWordNotFoundException_예외가_발생한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(() -> commentService.save(account.getId(), -1L, "댓글"))
                .isInstanceOf(AssociationWordNotFoundException.class)
                .hasMessage("댓글과 관련된 용어를 찾을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void save_메서드는_유효하지_않은_댓글_내용을_전달하면_InvalidCommentContentException_예외가_발생한다(String invalidContent) {
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

        // when & then
        assertThatThrownBy(() -> commentService.save(account.getId(), word.getId(), invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void save_메서드는_유효한_파라미터를_전달하면_댓글을_추가한다() {
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

        // when & then
        assertDoesNotThrow(() -> commentService.save(account.getId(), word.getId(), "댓글"));
    }

    @Test
    void delete_메서드는_없는_회원을_전달하면_AssociationAccountNotFoundException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> commentService.delete("accountId", -1L))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void delete_메서드는_지정한_댓글_ID가_없다면_CommentNotFoundException_예외가_발생한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(() -> commentService.delete(account.getId(), -1L))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void delete_메서드는_댓글_작성자가_아니라면_ForbiddenCommentException_예외가_발생한다() {
        // given
        Account writer = Account.builder()
                                .id("accountId1")
                                .nickname("nickname")
                                .profileImage("profileImage")
                                .roleName(Role.ROLE_ADMIN.name())
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();
        Account account = Account.builder()
                                 .id("accountId2")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(writer);
        accountRepository.save(account);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "댓글");

        // when & then
        assertThatThrownBy(() -> commentService.delete(account.getId(), 1L))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 삭제할 권한이 없습니다.");
    }

    @Test
    void delete_메서드는_유효한_파라미터를_전달하면_지정한_댓글을_싹제한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId1")
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
        commentService.save(account.getId(), word.getId(), "댓글");

        // when & then
        assertDoesNotThrow(() -> commentService.delete(account.getId(), 1L));
    }

    @Test
    void update_메서드는_없는_회원을_전달하면_AssociationAccountNotFoundException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> commentService.update("accountId", -1L, "댓글"))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void update_메서드는_지정한_댓글_ID가_없다면_CommentNotFoundException_예외가_발생한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(() -> commentService.update(account.getId(), -1L, "댓글"))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void update_메서드는_댓글_작성자가_아니라면_ForbiddenCommentException_예외가_발생한다() {
        // given
        Account writer = Account.builder()
                                .id("accountId1")
                                .nickname("nickname")
                                .profileImage("profileImage")
                                .roleName(Role.ROLE_ADMIN.name())
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("word meaning")
                        .categoryName("개발")
                        .build();
        Account account = Account.builder()
                                 .id("accountId2")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountRepository.save(writer);
        accountRepository.save(account);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "댓글");

        // when & then
        assertThatThrownBy(() -> commentService.update(account.getId(), 1L, "댓글"))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 수정할 권한이 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void update_메서드는_유효하지_않은_댓글_내용을_전달하면_InvalidCommentContentException_예외가_발생한다(String invalidContent) {
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
        commentService.save(account.getId(), word.getId(), "댓글");

        // when & then
        assertThatThrownBy(() -> commentService.update(account.getId(), word.getId(), invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void update_메서드는_유효한_파라미터를_전달하면_전달한_내용으로_댓글_내용을_변경한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId1")
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
        commentService.save(account.getId(), word.getId(), "댓글");

        // when & then
        assertDoesNotThrow(() -> commentService.update(account.getId(), 1L, "댓글 변경"));
    }

    @Test
    void readAllBy_메서드는_로그인한_경우_지정한_조건에_따라_댓글_목록을_조회한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId1")
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
        commentService.save(account.getId(), word.getId(), "댓글");

        // when
        List<ReadAllCommentDto> actual = commentService.readAllBy(
                null,
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).commentInfo().content()).isEqualTo("댓글")
        );
    }

    @Test
    void readAllBy_메서드는_로그인하지_않은_경우_지정한_조건에_따라_댓글_목록을_조회한다() {
        // given
        Account account = Account.builder()
                                 .id("accountId1")
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
        commentService.save(account.getId(), word.getId(), "댓글");

        // when
        List<ReadAllCommentDto> actual = commentService.readAllBy(
                account.getId(),
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).commentInfo().content()).isEqualTo("댓글")
        );
    }
}
