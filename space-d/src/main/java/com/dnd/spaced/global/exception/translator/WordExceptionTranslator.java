package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.WordErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WordExceptionTranslator implements ExceptionTranslator {
    INVALID_WORD_MEANING_EXCEPTION(
            WordErrorCode.INVALID_WORD_MEANING,
            HttpStatus.BAD_REQUEST,
            "용어 뜻은 최소 10글자 이상, 최대 70글자 이하여야 합니다."
    ),
    INVALID_CATEGORY_NAME_EXCEPTION(
            WordErrorCode.INVALID_CATEGORY_NAME,
            HttpStatus.BAD_REQUEST,
            "유효한 용어 카테고리 이름이 아닙니다."
    ),
    INVALID_WORD_NAME_EXCEPTION(
            WordErrorCode.INVALID_WORD_NAME,
            HttpStatus.BAD_REQUEST,
            "용어 이름은 null이거나 비어 있을 수 없습니다."
    ),
    INVALID_PRONUNCIATION_TYPE_NAME_EXCEPTION(
            WordErrorCode.INVALID_PRONUNCIATION_TYPE_NAME,
            HttpStatus.BAD_REQUEST,
            "유효한 용어 발음 타입이 아닙니다."
    ),
    INVALID_PRONUNCIATION_CONTENT_EXCEPTION(
            WordErrorCode.INVALID_PRONUNCIATION_CONTENT,
            HttpStatus.BAD_REQUEST,
            "발음은 null이거나 비어 있을 수 없습니다."
    ),
    INVALID_WORD_EXAMPLE_CONTENT_EXCEPTION(
            WordErrorCode.INVALID_WORD_EXAMPLE_CONTENT,
            HttpStatus.BAD_REQUEST,
            "예문의 길이는 최소 1글자 이상, 최대 50글자 이하여야 합니다."
    ),
    WORD_NOT_FOUND(
            WordErrorCode.WORD_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            "지정한 용어가 없습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    WordExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static WordExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(WordExceptionTranslator.values())
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
