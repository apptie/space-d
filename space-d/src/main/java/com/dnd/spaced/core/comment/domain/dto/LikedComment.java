package com.dnd.spaced.core.comment.domain.dto;

import com.dnd.spaced.core.comment.domain.Comment;

public record LikedComment(
        Comment comment,
        boolean isLiked,
        String writerNickname,
        String writerProfileImage
) {

    public LikedComment(Comment comment, String writerNickname, String writerProfileImage) {
       this(comment, false, writerNickname, writerProfileImage);
    }
}
