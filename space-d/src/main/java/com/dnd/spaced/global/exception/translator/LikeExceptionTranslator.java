package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.LikeErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LikeExceptionTranslator implements ExceptionTranslator{
    FORBIDDEN_LIKE_EXCEPTION(
            LikeErrorCode.FORBIDDEN_LIKE,
            HttpStatus.FORBIDDEN,
            "좋아요를 제어할 권한이 없습니다."
    ),
    ASSOCIATION_COMMENT_NOT_FOUND_EXCEPTION(
            LikeErrorCode.ASSOCIATION_COMMENT_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            "좋아요 대상인 댓글을 찾을 수 없습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    LikeExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static LikeExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(LikeExceptionTranslator.values())
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
