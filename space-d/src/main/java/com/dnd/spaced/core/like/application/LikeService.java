package com.dnd.spaced.core.like.application;

import com.dnd.spaced.core.comment.application.event.dto.LikedEvent;
import com.dnd.spaced.core.comment.application.event.dto.UnlikedEvent;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.like.application.exception.AssociationCommentNotFoundException;
import com.dnd.spaced.core.like.domain.Like;
import com.dnd.spaced.core.like.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void processLike(Long accountId, Long commentId) {
        Comment comment = findComment(commentId);

        likeRepository.findBy(accountId, comment.getId())
                      .ifPresentOrElse(
                              like -> deleteLike(like, comment),
                              () -> addLike(accountId, comment)
                      );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new AssociationCommentNotFoundException("좋아요 대상인 댓글을 찾을 수 없습니다."));
    }

    private void deleteLike(Like like, Comment comment) {
        likeRepository.delete(like);
        eventPublisher.publishEvent(new UnlikedEvent(comment.getId()));
    }

    private void addLike(Long accountId, Comment comment) {
        Like like = new Like(accountId, comment.getId());

        likeRepository.save(like);
        eventPublisher.publishEvent(new LikedEvent(comment.getId()));
    }
}
