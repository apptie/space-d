package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.ImageErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ImageExceptionTranslator implements ExceptionTranslator {
    IMAGE_FILE_NOT_FOUND_EXCEPTION(
            ImageErrorCode.IMAGE_FILE_NOT_FOUND_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "지정한 이미지 파일을 찾지 못했습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    ImageExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ImageExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(ImageExceptionTranslator.values())
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
