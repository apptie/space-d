package com.dnd.spaced.core.comment.application.dto.response;

import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.dto.response.LikedCommentDto;
import java.util.Map;

public record ReadAllCommentDto(CommentInfoDto commentInfo, WriterInfoDto writerInfo, boolean isLike) {

    public record CommentInfoDto(Long id, Long wordId, String content, int likeCount) {
    }

    public record WriterInfoDto(String id, String writerNickname, String writerProfileImage) {
    }

    public static ReadAllCommentDto of(LikedCommentDto dto, Map<Long, Integer> cacheLikeCount) {
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
                calculateLikeCount(
                        cacheLikeCount.compute(
                                comment.getId(),
                                (key, value) -> value == null ? comment.getLikeCount() : value
                        ),
                        dto.isLiked()
                )
        );

        return new ReadAllCommentDto(commentInfoDto, writerInfoDto, dto.isLiked());
    }

    private static int calculateLikeCount(int likeCount, boolean isLiked) {
        if (isLiked) {
            return likeCount + 1;
        }

        return likeCount;
    }
}
