package com.dnd.spaced.core.comment.domain.dto;

import com.dnd.spaced.core.comment.domain.Comment;

public record LikedCommentInfo(
        Comment comment,
        boolean isLiked,
        String writerNickname,
        String writerProfileImage
) {

    public LikedCommentInfo(Comment comment, String writerNickname, String writerProfileImage) {
       this(comment, false, writerNickname, writerProfileImage);
    }
}
