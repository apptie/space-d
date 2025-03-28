package com.dnd.spaced.config.docs.snippet.exceptions.comment;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.CommentErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/comments")
public class CommentExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<CommentExceptionDocs>> findExceptions() {
        CommentExceptionDocs commentExceptionDocs =
                CommentExceptionDocs.builder()
                                    .saveCommentException(calculateSaveCommentException())
                                    .updateCommentException(calculateUpdateCommentException())
                                    .deleteCommentException(calculateDeleteCommentException())
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(commentExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateSaveCommentException() {
        Map<String, ExceptionContent> saveCommentException = new LinkedHashMap<>();

        processCommentException(
                saveCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.INVALID_COMMENT_CONTENT,
                CommentErrorCode.WORD_NOT_FOUND_EXCEPTION
        );

        return saveCommentException;
    }

    private Map<String, ExceptionContent> calculateDeleteCommentException() {
        Map<String, ExceptionContent> deleteCommentException = new LinkedHashMap<>();

        processCommentException(
                deleteCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.FORBIDDEN_COMMENT
        );

        return deleteCommentException;
    }

    private Map<String, ExceptionContent> calculateUpdateCommentException() {
        Map<String, ExceptionContent> updateCommentException = new LinkedHashMap<>();

        processCommentException(
                updateCommentException,
                CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
                CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
                CommentErrorCode.FORBIDDEN_COMMENT,
                CommentErrorCode.INVALID_COMMENT_CONTENT
        );

        return updateCommentException;
    }
}
