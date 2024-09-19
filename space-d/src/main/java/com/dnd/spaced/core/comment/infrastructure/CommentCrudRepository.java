package com.dnd.spaced.core.comment.infrastructure;

import com.dnd.spaced.core.comment.domain.Comment;
import org.springframework.data.repository.CrudRepository;

interface CommentCrudRepository extends CrudRepository<Comment, Long> {
}
