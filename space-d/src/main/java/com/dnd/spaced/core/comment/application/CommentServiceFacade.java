package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceFacade {

    private final CreateCommentService createCommentService;
    private final ReadCommentService readCommentService;
    private final UpdateCommentService updateCommentService;
    private final DeleteCommentService deleteCommentService;

    @Transactional
    public void createComment(Long accountId, Long wordId, CreateCommentRequest request) {
        createCommentService.createComment(accountId, wordId, request);
    }

    @Transactional
    public void deleteComment(Long accountId, Long commentId) {
        deleteCommentService.deleteComment(accountId, commentId);
    }

    @Transactional
    public void updateComment(Long accountId, Long commentId, UpdateCommentRequest request) {
        updateCommentService.updateComment(accountId, commentId, request);
    }

    public CommentCollectionResponse readComments(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        return readCommentService.readComments(accountId, wordId, lastCommentId, pageable);
    }
}
