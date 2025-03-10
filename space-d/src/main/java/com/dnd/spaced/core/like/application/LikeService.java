package com.dnd.spaced.core.like.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.application.event.dto.LikedEvent;
import com.dnd.spaced.core.comment.application.event.dto.UnlikedEvent;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.application.exception.ForbiddenLikeException;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void processLike(Long accountId, Long commentId) {
        Account account = findAccount(accountId);
        Comment targetComment = findTargetComment(commentId);

        likeRepository.findBy(account.getId(), targetComment.getId())
                      .ifPresentOrElse(
                              like -> processDeleteLike(like, targetComment),
                              () -> processAddLike(account, targetComment)
                      );
    }

    private Comment findTargetComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new AssociationCommentNotFoundException("좋아요 대상인 댓글을 찾을 수 없습니다."));
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenLikeException("지정한 ID에 대한 회원을 찾지 못했습니다."));
    }

    private void processDeleteLike(Like like, Comment comment) {
        likeRepository.delete(like);
        publishDeletedLikeEvent(comment);
    }

    private void processAddLike(Account account, Comment comment) {
        likeRepository.save(new Like(account.getId(), comment.getId()));
        publishAddedLikeEvent(comment);
    }

    private void publishDeletedLikeEvent(Comment comment) {
        eventPublisher.publishEvent(new UnlikedEvent(comment.getId()));
    }

    private void publishAddedLikeEvent(Comment comment) {
        eventPublisher.publishEvent(new LikedEvent(comment.getId()));
    }
}
