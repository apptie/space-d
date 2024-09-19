package com.dnd.spaced.core.comment.presentation.dto.response;

import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import java.util.List;

public record ReadAllCommentResponse(List<CommentInfoResponse> comments) {

    private record CommentInfoResponse(DetailCommentInfoResponse commentInfo, WriterInfoResponse writerInfo) {

        private record DetailCommentInfoResponse(Long id, Long wordId, String content, int likeCount) {
        }

        private record WriterInfoResponse(String id, String writerNickname, String writerProfileImage) {
        }

        static CommentInfoResponse from(ReadAllCommentDto dto) {
            DetailCommentInfoResponse detailCommentInfoResponse = new DetailCommentInfoResponse(
                    dto.commentInfo().id(),
                    dto.commentInfo().wordId(),
                    dto.commentInfo().content(),
                    dto.commentInfo().likeCount()
            );
            WriterInfoResponse writerInfoResponse = new WriterInfoResponse(
                    dto.writerInfo().id(),
                    dto.writerInfo().writerNickname(),
                    dto.writerInfo().writerProfileImage()
            );
            return new CommentInfoResponse(
                    detailCommentInfoResponse,
                    writerInfoResponse
            );
        }
    }

    public static ReadAllCommentResponse from(List<ReadAllCommentDto> dtos) {
        List<CommentInfoResponse> comments = dtos.stream()
                                                 .map(CommentInfoResponse::from)
                                                 .toList();

        return new ReadAllCommentResponse(comments);
    }
}
