package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.BookmarkErrorCode;
import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BookmarkExceptionTranslator implements ExceptionTranslator {
    BOOKMARK_NOT_FOUND_EXCEPTION(
            BookmarkErrorCode.BOOKMARK_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "지정한 식별자의 북마크를 찾지 못했습니다."
    ),
    WORD_NOT_FOUND_EXCEPTION(
            BookmarkErrorCode.WORD_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "지정한 식별자의 용어를 찾지 못했습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    BookmarkExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static BookmarkExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(BookmarkExceptionTranslator.values())
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
