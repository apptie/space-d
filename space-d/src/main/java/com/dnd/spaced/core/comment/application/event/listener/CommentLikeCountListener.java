package com.dnd.spaced.core.comment.application.event.listener;

import com.dnd.spaced.core.comment.application.event.dto.LikedEvent;
import com.dnd.spaced.core.comment.application.event.dto.UnlikedEvent;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CommentLikeCountListener {

    private final CommentRepository commentRepository;

    @EventListener
    @Transactional
    public void listen(LikedEvent event) {
        commentRepository.increaseLikeCount(event.commentId());
    }

    @EventListener
    @Transactional
    public void listen(UnlikedEvent event) {
        commentRepository.decreaseLikeCount(event.commentId());
    }
}
