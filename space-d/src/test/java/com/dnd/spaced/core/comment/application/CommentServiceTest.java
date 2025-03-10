package com.dnd.spaced.core.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.exception.AssociationAccountNotFoundException;
import com.dnd.spaced.core.comment.application.exception.AssociationWordNotFoundException;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.application.helper.WithWriterAndReaderAndWordTestHelper;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import java.util.Optional;
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
class CommentServiceTest extends WithWriterAndReaderAndWordTestHelper {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeRepository likeRepository;

    @Test
    void 없거나_탈퇴한_회원_식별자로는_댓글을_작성할_수_없다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.createComment(-999L, word.getId(), request))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 댓글을_작성할_용어가_없는_경우_댓글을_작성할_수_없다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when & then
        assertThatThrownBy(() -> commentService.createComment(writer.getId(), -1L, request))
                .isInstanceOf(AssociationWordNotFoundException.class)
                .hasMessage("댓글과 관련된 용어를 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 댓글을 작성할 수 없다")
    @NullAndEmptySource
    void 유효한_길이의_댓글_내용이_아니라면_댓글을_작성할_수_없다(String invalidContent) {
        // given
        CreateCommentRequest request = new CreateCommentRequest(invalidContent);

        // when & then
        assertThatThrownBy(() -> commentService.createComment(writer.getId(), word.getId(), request))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글을_작성한다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");

        // when
        commentService.createComment(writer.getId(), word.getId(), request);

        // then
        Optional<Comment> actual = commentRepository.findBy(1L);

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().isWriter(writer.getId())).isTrue(),
                () -> assertThat(actual.get().getContent()).isEqualTo("이 용어는 언제 쓰는건가요?")
        );
    }

    @Test
    void 없거나_탈퇴한_회원_식별자로는_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(-1L, 1L))
                .isInstanceOf(AssociationAccountNotFoundException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 없는_댓글_식별자를_통해_댓글을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(writer.getId(), -1L))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void 댓글_작성자가_아니라면_댓글을_삭제할_수_없다() {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(reader.getId(), 1L))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 삭제할 권한이 없습니다.");
    }

    @Test
    void 댓글을_삭제한다() {
        // given
        CreateCommentRequest request = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), request);

        // when & then
        assertDoesNotThrow(() -> commentService.deleteComment(writer.getId(), 1L));
    }

    @Test
    void 없거나_탈퇴한_회원의_식별자로는_댓글을_수정할_수_없다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() ->
                commentService.updateComment(
                        -999L,
                        1L,
                        request
                )
        ).isInstanceOf(AssociationAccountNotFoundException.class)
         .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    void 식별할_수_없는_댓글_식별자로_댓글을_수정할_수_없다() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(writer.getId(), -999L, request))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 댓글이 없습니다.");
    }

    @Test
    void 댓글_작성자가_아니라면_댓글을_수정할_수_없다() {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest);
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(reader.getId(), 1L, request))
                .isInstanceOf(ForbiddenCommentException.class)
                .hasMessage("댓글을 수정할 권한이 없습니다.");
    }

    @ParameterizedTest(name = "댓글 내용이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 비어_있는_내용으로_댓글을_수정할_수_없다(String invalidContent) {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest);
        UpdateCommentRequest request = new UpdateCommentRequest(invalidContent);

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(writer.getId(), word.getId(), request))
                .isInstanceOf(InvalidCommentContentException.class)
                .hasMessage("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
    }

    @Test
    void 댓글을_수정한다() {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest);
        UpdateCommentRequest request = new UpdateCommentRequest("처음 보는 용어인데 잘 쓰지는 않나보네요");

        // when & then
        assertDoesNotThrow(() -> commentService.updateComment(writer.getId(), 1L, request));
    }

    @Test
    void 로그인_하지_않고_특정_용어의_댓글_목록을_조회한다() {
        // given
        CreateCommentRequest createCommentRequest1 = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest1);
        CreateCommentRequest createCommentRequest2 = new CreateCommentRequest("쓰는걸 본 적이 없는 것 같네요");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest2);
        Like like = new Like(writer.getId(), 1L);
        likeRepository.save(like);

        // when
        CommentCollectionResponse actual = commentService.readComments(
                null,
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.comments()).hasSize(2),
                () -> assertThat(actual.comments().get(0).commentContent().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.comments().get(0).liked()).isFalse(),
                () -> assertThat(actual.comments().get(1).commentContent().content()).isEqualTo("쓰는걸 본 적이 없는 것 같네요"),
                () -> assertThat(actual.comments().get(1).liked()).isFalse()
        );
    }

    @Test
    void 로그인하고_특정_용어의_댓글_목록을_조회한다() {
        // given
        CreateCommentRequest createCommentRequest1 = new CreateCommentRequest("이 용어는 언제 쓰는건가요?");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest1);
        CreateCommentRequest createCommentRequest2 = new CreateCommentRequest("쓰는걸 본 적이 없는 것 같네요");
        commentService.createComment(writer.getId(), word.getId(), createCommentRequest2);
        Like like = new Like(writer.getId(), 1L);
        likeRepository.save(like);

        // when
        CommentCollectionResponse actual = commentService.readComments(
                writer.getId(),
                word.getId(),
                null,
                PageRequest.of(0, 10)
        );

        // then
        assertAll(
                () -> assertThat(actual.comments()).hasSize(2),
                () -> assertThat(actual.comments().get(0).commentContent().content()).isEqualTo("이 용어는 언제 쓰는건가요?"),
                () -> assertThat(actual.comments().get(0).liked()).isTrue(),
                () -> assertThat(actual.comments().get(1).commentContent().content()).isEqualTo("쓰는걸 본 적이 없는 것 같네요"),
                () -> assertThat(actual.comments().get(1).liked()).isFalse()
        );
    }
}
