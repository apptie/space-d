package com.dnd.spaced.core.comment.application.dto.mapper;

import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentContentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentResponse;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse.CommentWriterResponse;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.dto.LikedComment;
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public class CommentResponseCollectionMapper {

    public CommentCollectionResponse toDto(List<LikedComment> comments) {
        if (comments.isEmpty()) {
            return new CommentCollectionResponse(List.of(), null);
        }

        List<CommentResponse> responses = comments.stream()
                                                  .map(this::toCommentResponse)
                                                  .toList();

        return new CommentCollectionResponse(responses, comments.get(comments.size() - 1).comment().getId());
    }

    private CommentResponse toCommentResponse(LikedComment likedComment) {
        return new CommentResponse(
                toCommentContentResponse(likedComment.comment()),
                toCommentWriterResponse(
                        likedComment.writerNickname(),
                        likedComment.writerProfileImage(),
                        likedComment.comment().getWriterId()
                ),
                likedComment.isLiked()
        );
    }

    private CommentContentResponse toCommentContentResponse(Comment comment) {
        return new CommentContentResponse(
                comment.getId(),
                comment.getWordId(),
                comment.getContent(),
                comment.getLikeCount()
        );
    }

    private CommentWriterResponse toCommentWriterResponse(
            String writerNickname,
            String writerProfileImage,
            Long writerId
    ) {
        return new CommentWriterResponse(writerId, writerNickname, writerProfileImage);
    }
}
