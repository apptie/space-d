package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.application.dto.mapper.CommentApplicationMapper;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.exception.AssociationAccountNotFoundException;
import com.dnd.spaced.core.comment.application.exception.AssociationWordNotFoundException;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final WordRepository wordRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Long accountId, Long wordId, CreateCommentRequest request) {
        Account writer = findAccount(accountId);
        Word word = findWord(wordId);
        Comment comment = new Comment(writer.getId(), word.getId(), request.content());

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long accountId, Long commentId) {
        Account writer = findAccount(accountId);
        Comment comment = findComment(commentId);

        validateDeleteAuthority(comment, writer);

        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Long accountId, Long commentId, UpdateCommentRequest request) {
        Account writer = findAccount(accountId);
        Comment comment = findComment(commentId);

        validateUpdateAuthority(comment, writer);
        comment.changeContent(request.content());
    }

    public CommentCollectionResponse readComments(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        List<LikedCommentDto> comments = commentRepository.findAllBy(accountId, wordId, lastCommentId, pageable);

        return CommentApplicationMapper.toDto(comments);
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new AssociationAccountNotFoundException("유효하지 않은 회원입니다."));
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new AssociationWordNotFoundException("댓글과 관련된 용어를 찾을 수 없습니다."));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("지정한 ID에 해당하는 댓글이 없습니다."));
    }

    private void validateDeleteAuthority(Comment comment, Account writer) {
        if (comment.isNotWriter(writer)) {
            throw new ForbiddenCommentException("댓글을 삭제할 권한이 없습니다.");
        }
    }

    private void validateUpdateAuthority(Comment comment, Account writer) {
        if (comment.isNotWriter(writer)) {
            throw new ForbiddenCommentException("댓글을 수정할 권한이 없습니다.");
        }
    }
}
