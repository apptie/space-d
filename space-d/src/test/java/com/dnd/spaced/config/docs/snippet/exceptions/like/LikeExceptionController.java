package com.dnd.spaced.config.docs.snippet.exceptions.like;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.LikeErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/likes")
public class LikeExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<LikeExceptionDocs>> findExceptions() {
        LikeExceptionDocs likeExceptionDocs =
                LikeExceptionDocs.builder()
                                 .processLikeException(calculateProcessLikeException())
                                    .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(likeExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateProcessLikeException() {
        Map<String, ExceptionContent> processLikeException = new LinkedHashMap<>();

        processLikeException(
                processLikeException,
                LikeErrorCode.ASSOCIATION_COMMENT_NOT_FOUND
        );

        return processLikeException;
    }
}
