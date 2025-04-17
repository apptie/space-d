package com.dnd.spaced.core.comment.application;

import com.dnd.spaced.core.comment.application.dto.mapper.CommentResponseCollectionMapper;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.exception.CommentNotFoundException;
import com.dnd.spaced.core.comment.application.exception.ForbiddenCommentException;
import com.dnd.spaced.core.comment.application.exception.WordNotFoundException;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedCommentInfo;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final WordRepository wordRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Long accountId, Long wordId, CreateCommentRequest request) {
        validateWordId(wordId);

        Comment comment = new Comment(accountId, wordId, request.content());

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long accountId, Long commentId) {
        Comment comment = findComment(commentId);

        validateDeleteAuthority(comment, accountId);
        comment.delete();
    }

    @Transactional
    public void updateComment(Long accountId, Long commentId, UpdateCommentRequest request) {
        Comment comment = findComment(commentId);

        validateUpdateAuthority(comment, accountId);
        comment.changeContent(request.content());
    }

    public CommentCollectionResponse readComments(Long accountId, Long wordId, Long lastCommentId, Pageable pageable) {
        List<LikedCommentInfo> comments = commentRepository.findAllBy(accountId, wordId, lastCommentId, pageable);

        return CommentResponseCollectionMapper.toCollectionDto(comments);
    }

    private void validateWordId(Long wordId) {
        if (!wordRepository.existsBy(wordId)) {
            throw new WordNotFoundException("댓글과 관련된 용어를 찾을 수 없습니다.");
        }
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findBy(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("지정한 ID에 해당하는 댓글이 없습니다."));
    }

    private void validateDeleteAuthority(Comment comment, Long accountId) {
        if (comment.isNotWriter(accountId)) {
            throw new ForbiddenCommentException("댓글을 삭제할 권한이 없습니다.");
        }
    }

    private void validateUpdateAuthority(Comment comment, Long accountId) {
        if (comment.isNotWriter(accountId)) {
            throw new ForbiddenCommentException("댓글을 수정할 권한이 없습니다.");
        }
    }
}
