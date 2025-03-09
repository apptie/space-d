package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.SkillErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SkillExceptionTranslator implements ExceptionTranslator {
    SKILL_NOT_FOUND_EXCEPTION(
            SkillErrorCode.SKILL_NOT_FOUND_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "회원의 스킬 정보가 정상적으로 초기화되지 않았습니다."
    ),
    QUIZ_METADATA_NOT_FOUND_EXCEPTION(
            SkillErrorCode.QUIZ_METADATA_NOT_FOUND_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "퀴즈 메타데이터 정보가 정상적으로 초기화되지 않았습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    SkillExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static SkillExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(SkillExceptionTranslator.values())
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
