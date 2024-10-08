package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.AuthErrorCode;
import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthExceptionTranslator implements ExceptionTranslator{
    INVALID_BLACKLIST_TOKEN_CONTENT_EXCEPTION(
            AuthErrorCode.INVALID_BLACKLIST_TOKEN_CONTENT,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "토큰 처리 과정에서 문제가 발생했습니다."
    ),
    INVALID_TOKEN_EXCEPTION(
            AuthErrorCode.INVALID_TOKEN,
            HttpStatus.UNAUTHORIZED,
            "유효한 토큰이 아닙니다."
    ),
    FORBIDDEN_INIT_CAREER_INFO_EXCEPTION(
            AuthErrorCode.FORBIDDEN_INIT_CAREER_INFO,
            HttpStatus.FORBIDDEN,
            "권한이 없습니다."
    ),
    REFRESH_TOKEN_NOT_FOUND_EXCEPTION(
            AuthErrorCode.REFRESH_TOKEN_NOT_FOUND,
            HttpStatus.UNAUTHORIZED,
            "refresh token cookie가 없습니다."
    ),
    EXPIRED_TOKEN_EXCEPTION(
            AuthErrorCode.EXPIRED_TOKEN,
            HttpStatus.UNAUTHORIZED,
            "토큰이 만료되었습니다."
    ),
    BLOCKED_TOKEN_EXCEPTION(
            AuthErrorCode.BLOCKED_TOKEN,
            HttpStatus.UNAUTHORIZED,
            "세션이 만료되었습니다. 다시 로그인 해 주세요."
    ),
    ROTATION_REFRESH_TOKEN_MISMATCH_EXCEPTION(
            AuthErrorCode.ROTATION_REFRESH_TOKEN_MISMATCH,
            HttpStatus.UNAUTHORIZED,
            "세션이 만료되었습니다. 다시 로그인 해 주세요."
    )

    ;

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    AuthExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static AuthExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(AuthExceptionTranslator.values())
                     .filter(translator -> translator.errorCode == errorCode)
                     .findAny()
                     .orElseThrow(
                             () -> new IllegalStateException(
                                     errorCode.toString() + "으로 정의된 예외 번역기가 존재하지 않습니다."
                             )
                     );
    }

    @Override
    public ExceptionDto translate() {
        return new ExceptionDto(this.errorCode.toString(), this.message);
    }
}
