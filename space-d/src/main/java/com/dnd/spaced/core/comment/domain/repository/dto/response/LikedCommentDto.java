package com.dnd.spaced.core.comment.domain.repository.dto.response;

import com.dnd.spaced.core.comment.domain.Comment;

public record LikedCommentDto(Comment comment, boolean isLiked, String writerNickname, String writerProfileImage) {
}
