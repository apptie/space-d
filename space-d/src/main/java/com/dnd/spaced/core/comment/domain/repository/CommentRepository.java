package com.dnd.spaced.core.comment.domain.repository;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedCommentInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findBy(Long commentId);

    List<LikedCommentInfo> findAllBy(Long accountId, Long wordId, Long lastCommentId, Pageable pageable);

    void delete(Comment comment);

    void increaseLikeCount(Long commentId);

    void decreaseLikeCount(Long commentId);
}
