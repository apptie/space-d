package com.dnd.spaced.core.comment.application.dto.mapper;

import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentContentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentWriterResponse;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedCommentInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentResponseCollectionMapper {

    public static CommentCollectionResponse toCollectionDto(List<LikedCommentInfo> comments) {
        if (comments.isEmpty()) {
            return new CommentCollectionResponse(List.of(), null);
        }

        List<CommentResponse> responses = comments.stream()
                                                  .map(CommentResponseCollectionMapper::toCommentResponse)
                                                  .toList();

        return new CommentCollectionResponse(responses, comments.get(comments.size() - 1).comment().getId());
    }

    private static CommentResponse toCommentResponse(LikedCommentInfo likedCommentInfo) {
        return new CommentResponse(
                toCommentContentResponse(likedCommentInfo.comment()),
                toCommentWriterResponse(
                        likedCommentInfo.writerNickname(),
                        likedCommentInfo.writerProfileImage(),
                        likedCommentInfo.comment().getWriterId()
                ),
                likedCommentInfo.isLiked()
        );
    }

    private static CommentContentResponse toCommentContentResponse(Comment comment) {
        return new CommentContentResponse(
                comment.getId(),
                comment.getWordId(),
                comment.getContent(),
                comment.getLikeCount()
        );
    }

    private static CommentWriterResponse toCommentWriterResponse(
            String writerNickname,
            String writerProfileImage,
            Long writerId
    ) {
        return new CommentWriterResponse(writerId, writerNickname, writerProfileImage);
    }
}
