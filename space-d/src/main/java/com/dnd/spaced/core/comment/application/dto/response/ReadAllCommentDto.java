package com.dnd.spaced.core.comment.application.dto.response;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;

public record ReadAllCommentDto(CommentInfoDto commentInfo, WriterInfoDto writerInfo, boolean isLike) {

    public record CommentInfoDto(Long id, Long wordId, String content, int likeCount) {
    }

    public record WriterInfoDto(Long id, String writerNickname, String writerProfileImage) {
    }

    public static ReadAllCommentDto from(LikedCommentDto dto) {
        Comment comment = dto.comment();
        WriterInfoDto writerInfoDto = new WriterInfoDto(
                comment.getAccountId(),
                dto.writerNickname(),
                dto.writerProfileImage()
        );
        CommentInfoDto commentInfoDto = new CommentInfoDto(
                comment.getId(),
                comment.getWordId(),
                comment.getContent(),
                comment.getLikeCount()
        );

        return new ReadAllCommentDto(commentInfoDto, writerInfoDto, dto.isLiked());
    }
}
