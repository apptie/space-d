package com.dnd.spaced.core.like.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.application.exception.ForbiddenLikeException;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeCountRepository;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final LikeCountRepository likeCountRepository;

    @Transactional
    public void processLike(String accountId, Long commentId) {
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

    private Account findAccount(String accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenLikeException("좋아요를 제어할 권한이 없습니다."));
    }

    private void processDeleteLike(Like like, Comment comment) {
        likeRepository.delete(like);
        likeCountRepository.deleteLikeCount(comment.getWordId(), comment.getId());
    }

    private void processAddLike(Account account, Comment comment) {
        likeRepository.save(new Like(account.getId(), comment.getId()));
        likeCountRepository.addLikeCount(comment.getWordId(), comment.getId());
    }
}
