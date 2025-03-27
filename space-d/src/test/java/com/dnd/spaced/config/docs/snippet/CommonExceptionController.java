package com.dnd.spaced.config.docs.snippet;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
import java.util.Map;
import org.springframework.http.HttpStatus;

public abstract class CommonExceptionController {

    protected void processExceptionContent(Map<String, ExceptionContent> target, ExceptionTranslator translator) {
        ExceptionDto exceptionDto = translator.translate();
        HttpStatus httpStatus = translator.getHttpStatus();
        ExceptionContent exceptionContent = new ExceptionContent(
                httpStatus,
                exceptionDto.message()
        );

        target.put(exceptionDto.code(), exceptionContent);
    }

    protected void putUnauthorizedExceptionContent(Map<String, ExceptionContent> target) {
        target.put(
                "UNAUTHORIZED",
                new ExceptionContent(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다.")
        );
    }

    protected void putForbiddenExceptionContent(Map<String, ExceptionContent> target) {
        target.put("FORBIDDEN", new ExceptionContent(HttpStatus.FORBIDDEN, "권한이 없습니다."));
    }

    protected void putMethodArgumentNotValidExceptionContent(Map<String, ExceptionContent> target, String... inputs) {
        target.put("INVALID_DATA", createMethodArgumentNotValidExceptionContent(inputs));
    }

    protected ExceptionContent createMethodArgumentNotValidExceptionContent(String... inputs) {
        return new ExceptionContent(
                HttpStatus.BAD_REQUEST,
                "유효한 입력 값이 아닙니다. (" + String.join(", ", inputs) + ")"
        );
    }
}
