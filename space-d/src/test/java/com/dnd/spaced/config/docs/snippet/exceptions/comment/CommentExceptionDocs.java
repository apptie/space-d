package com.dnd.spaced.config.docs.snippet.exceptions.comment;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record CommentExceptionDocs(
        Map<String, ExceptionContent> createCommentException,
        Map<String, ExceptionContent> deleteCommentException,
        Map<String, ExceptionContent> updateCommentException
) {
}
