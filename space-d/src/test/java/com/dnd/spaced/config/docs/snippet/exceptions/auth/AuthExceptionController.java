package com.dnd.spaced.config.docs.snippet.exceptions.auth;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
import com.dnd.spaced.global.exception.translator.AuthExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/auths")
public class AuthExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<AuthExceptionDocs>> findExceptions() {
        AuthExceptionDocs authExceptionDocs =
                AuthExceptionDocs.builder()
                                 .refreshTokenException(calculateRefreshTokenException())
                                 .registerBlacklistTokenException(calculateRegisterBlacklistTokenException())
                                 .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(authExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateRegisterBlacklistTokenException() {
        Map<String, ExceptionContent> registerBlacklistTokenException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(registerBlacklistTokenException);
        putForbiddenExceptionContent(registerBlacklistTokenException);
        putMethodArgumentNotValidExceptionContent(registerBlacklistTokenException, "accountId");
        processAuthException(registerBlacklistTokenException, AuthErrorCode.INVALID_BLACKLIST_TOKEN_CONTENT_EXCEPTION);

        return registerBlacklistTokenException;
    }

    private Map<String, ExceptionContent> calculateRefreshTokenException() {
        Map<String, ExceptionContent> refreshTokenException = new LinkedHashMap<>();

        processAuthException(
                refreshTokenException,
                AuthErrorCode.REFRESH_TOKEN_NOT_FOUND_EXCEPTION,
                AuthErrorCode.EXPIRED_TOKEN_EXCEPTION,
                AuthErrorCode.BLOCKED_TOKEN_EXCEPTION,
                AuthErrorCode.ROTATION_REFRESH_TOKEN_MISMATCH_EXCEPTION
        );

        return refreshTokenException;
    }

    private void processAuthException(Map<String, ExceptionContent> target, AuthErrorCode... errorCodes) {
        for (AuthErrorCode errorCode : errorCodes) {
            ExceptionTranslator translator = AuthExceptionTranslator.findBy(errorCode);

            processExceptionContent(target, translator);
        }
    }
}
