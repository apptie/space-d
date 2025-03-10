package com.dnd.spaced.core.comment.application.dto.response;

import java.util.List;

public record CommentCollectionResponse(List<CommentResponse> comments, Long lastCommentId) {

    public record CommentResponse(CommentContentResponse commentContent, CommentWriterResponse writer, boolean liked) {
    }

    public record CommentContentResponse(Long commentId, Long wordId, String content, long likeCount) {
    }

    public record CommentWriterResponse(Long writerId, String writerNickname, String writerProfileImage) {
    }
}
