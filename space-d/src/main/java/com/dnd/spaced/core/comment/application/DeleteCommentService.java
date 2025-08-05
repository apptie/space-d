package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class DeleteCommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void deleteComment(Long accountId, Long commentId) {
        Comment comment = findComment(commentId);

        validateCommentDeletePermission(comment, accountId);
        executeCommentDeletion(comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("지정한 ID에 해당하는 댓글이 없습니다."));
    }

    private void validateCommentDeletePermission(Comment comment, Long accountId) {
        if (comment.isReader(accountId)) {
            throw new ForbiddenCommentException("댓글을 삭제할 권한이 없습니다.");
        }
    }

    private void executeCommentDeletion(Comment comment) {
        comment.delete();
    }
}
