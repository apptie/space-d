package com.dnd.spaced.config.docs.snippet.exceptions.auth;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.AccountErrorCode;
import com.dnd.spaced.global.exception.code.AuthErrorCode;
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
                                 .initAccountCareerInfoException(calculateInitAccountCareerInfoException())
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

    private Map<String, ExceptionContent> calculateInitAccountCareerInfoException() {
        Map<String, ExceptionContent> authProfileException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(authProfileException);
        putForbiddenExceptionContent(authProfileException);
        putMethodArgumentNotValidExceptionContent(
                authProfileException,
                "jobGroupName",
                "companyName",
                "experienceName"
        );

        processAccountException(
                authProfileException,
                AccountErrorCode.INVALID_COMPANY,
                AccountErrorCode.INVALID_EXPERIENCE,
                AccountErrorCode.INVALID_JOB_GROUP,
                AccountErrorCode.FORBIDDEN_INIT_CAREER_INFO_EXCEPTION
        );

        return authProfileException;
    }
}
