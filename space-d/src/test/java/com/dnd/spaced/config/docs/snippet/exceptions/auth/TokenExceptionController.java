package com.dnd.spaced.config.docs.snippet.exceptions.auth;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/tokens")
public class TokenExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<TokenExceptionDocs>> findExceptions() {
        TokenExceptionDocs tokenExceptionDocs =
                TokenExceptionDocs.builder()
                                 .commonTokenException(calculateCommonTokenException())
                                 .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(tokenExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateCommonTokenException() {
        Map<String, ExceptionContent> refreshTokenException = new LinkedHashMap<>();

        processAuthException(
                refreshTokenException,
                AuthErrorCode.FAILED_ENCODE_TOKEN_EXCEPTION,
                AuthErrorCode.FAILED_DECODE_TOKEN_EXCEPTION,
                AuthErrorCode.INVALID_TOKEN_EXCEPTION
        );

        return refreshTokenException;
    }
}
