package com.dnd.spaced.core.comment.domain.repository.dto.request;

import org.springframework.data.domain.Pageable;

public record CommentPageRequest(Pageable pageable, Long lastCommentId) {
}
