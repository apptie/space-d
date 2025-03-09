package com.dnd.spaced.core.comment.domain.repository;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.dto.request.CommentPageRequest;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findBy(Long id);

    List<LikedCommentDto> findAllBy(Long accountId, Long wordId, CommentPageRequest pageRequest);

    void delete(Comment comment);

    void increaseLikeCount(Long commentId);

    void decreaseLikeCount(Long commentId);
}
