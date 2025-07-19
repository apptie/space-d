package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UpdateCommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void updateComment(Long accountId, Long commentId, UpdateCommentRequest request) {
        Comment comment = findComment(commentId);

        validateCommentUpdatePermission(comment, accountId);
        executeCommentUpdate(request, comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("지정한 ID에 해당하는 댓글이 없습니다."));
    }

    private void validateCommentUpdatePermission(Comment comment, Long accountId) {
        if (comment.isReader(accountId)) {
            throw new ForbiddenCommentException("댓글을 수정할 권한이 없습니다.");
        }
    }

    private void executeCommentUpdate(UpdateCommentRequest request, Comment comment) {
        comment.changeContent(request.content());
    }
}
