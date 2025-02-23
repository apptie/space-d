package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.application.exception.AssociationAccountNotFoundException;
import com.dnd.spaced.core.comment.application.exception.AssociationWordNotFoundException;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
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

    @Test
    void 없거나_탈퇴한_회원_식별자로는_댓글을_작성할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.save(1L, 1L, "이 용어는 언제 쓰는건가요?"))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 댓글을_작성할_용어가_없는_경우_댓글을_작성할_수_없다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();

        accountRepository.save(writer);

        // when & then
        assertThatThrownBy(() -> commentService.save(writer.getId(), -1L, "이 용어는 언제 쓰는건가요?"))
                .isInstanceOf(AssociationWordNotFoundException.class)
                .hasMessage("댓글과 관련된 용어를 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 댓글을 작성할 수 없다")
    @NullAndEmptySource
    void 유효한_길이의_댓글_내용이_아니라면_댓글을_작성할_수_없다(String invalidContent) {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);

        // when & then
        assertThatThrownBy(() -> commentService.save(writer.getId(), word.getId(), invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글을_작성한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);

        // when & then
        assertDoesNotThrow(() ->
                commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?")
        );
    }

    @Test
    void 없거나_탈퇴한_회원_식별자로는_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.delete(1L, 1L))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 없는_댓글_식별자를_통해_댓글을_삭제할_수_없다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        accountRepository.save(account);

        // when & then
        assertThatThrownBy(() -> commentService.delete(account.getId(), -1L))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void 댓글_작성자가_아니라면_댓글을_삭제할_수_없다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();
        Account reader = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("54321")
                                .nickname("재빠른지구002")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();

        accountRepository.save(writer);
        accountRepository.save(reader);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.delete(reader.getId(), 1L))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 삭제할 권한이 없습니다.");
    }

    @Test
    void 댓글을_삭제한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        // when & then
        assertDoesNotThrow(() -> commentService.delete(writer.getId(), 1L));
    }

    @Test
    void 없거나_탈퇴한_회원의_식별자로는_댓글을_수정할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                commentService.update(
                        1L,
                        1L,
                        "처음 보는 용어인데 잘 쓰지는 않나보네요")
        ).isInstanceOf(AssociationAccountNotFoundException.class)
         .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 식별할_수_없는_댓글_식별자로_댓글을_수정할_수_없다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();

        accountRepository.save(writer);

        // when & then
        assertThatThrownBy(() -> commentService.update(writer.getId(), 1L, "이 용어는 언제 쓰는건가요?"))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void 댓글_작성자가_아니라면_댓글을_수정할_수_없다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();
        Account reader = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("54321")
                                .nickname("재빠른지구002")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();

        accountRepository.save(writer);
        accountRepository.save(reader);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.update(reader.getId(), 1L, "처음 보는 용어인데 잘 쓰지는 않나보네요"))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 수정할 권한이 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 비어_있는_내용으로_댓글을_수정할_수_없다(String invalidContent) {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.update(writer.getId(), word.getId(), invalidContent))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글을_수정한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        // when & then
        assertDoesNotThrow(() -> commentService.update(writer.getId(), 1L, "처음 보는 용어인데 잘 쓰지는 않나보네요"));
    }

    @Test
    void 로그인_하지_않고_특정_용어의_댓글_목록을_조회한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");
        commentService.save(writer.getId(), word.getId(), "쓰는걸 본 적이 없는 것 같네요");

        // when
        List<ReadAllCommentDto> actual = commentService.readAllBy(
                null,
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).commentInfo().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.get(1).commentInfo().content()).isEqualTo("쓰는걸 본 적이 없는 것 같네요")
        );
    }

    @Test
    void 로그인하고_특정_용어의_댓글_목록을_조회한다() {
        // given
        Account writer = Account.builder()
                                .registrationId(RegistrationId.KAKAO)
                                .socialIdentifier("12345")
                                .nickname("재빠른지구001")
                                .profileImage("earth.png")
                                .role(Role.ROLE_USER)
                                .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        accountRepository.save(writer);
        wordRepository.save(word);
        commentService.save(writer.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");
        commentService.save(writer.getId(), word.getId(), "쓰는걸 본 적이 없는 것 같네요");

        // when
        List<ReadAllCommentDto> actual = commentService.readAllBy(
                writer.getId(),
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).commentInfo().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.get(1).commentInfo().content()).isEqualTo("쓰는걸 본 적이 없는 것 같네요")
        );
    }
}
