package com.dnd.spaced.core.comment.domain.repository;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findBy(Long commentId);

    List<LikedComment> findAllBy(Long accountId, Long wordId, Long lastCommentId, Pageable pageable);

    void addLikeCount(Long commentId);

    void subtractLikeCount(Long commentId);
}
