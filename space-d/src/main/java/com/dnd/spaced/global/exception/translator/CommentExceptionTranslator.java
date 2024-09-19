package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.CommentErrorCode;
import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentExceptionTranslator implements ExceptionTranslator {
    ASSOCIATION_ACCOUNT_NOT_FOUND_EXCEPTION(
            CommentErrorCode.ASSOCIATION_ACCOUNT_NOT_FOUND,
            HttpStatus.FORBIDDEN,
            "유효하지 않은 회원입니다."
    ),
    ASSOCIATION_WORD_NOT_FOUND_EXCEPTION(
            CommentErrorCode.ASSOCIATION_WORD_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            "댓글과 관련된 용어를 찾을 수 없습니다."
    ),
    COMMENT_NOT_FOUND_EXCEPTION(
            CommentErrorCode.COMMENT_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            "지정한 ID에 해당하는 댓글이 없습니다."
    ),
    FORBIDDEN_COMMENT_EXCEPTION(
            CommentErrorCode.FORBIDDEN_COMMENT,
            HttpStatus.FORBIDDEN,
            "요청을 수행할 수 있는 권한이 없습니다."
    ),
    INVALID_COMMENT_CONTENT_EXCEPTION(
            CommentErrorCode.INVALID_COMMENT_CONTENT,
            HttpStatus.BAD_REQUEST,
            "댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    CommentExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static CommentExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(CommentExceptionTranslator.values())
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
