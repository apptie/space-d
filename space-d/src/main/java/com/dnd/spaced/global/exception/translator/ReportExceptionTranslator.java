package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.ReportErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReportExceptionTranslator implements ExceptionTranslator {
    REPORT_REASON_NOT_FOUND_EXCEPTION(
            ReportErrorCode.REPORT_REASON_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "지정한 원인의 신고 사유를 찾지 못했습니다."
    ),
    COMMENT_NOT_FOUND_EXCEPTION(
            ReportErrorCode.COMMENT_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "신고하려는 댓글을 찾을 수 없습니다."
    ),
    CANNOT_REPORT_OWN_COMMENT_EXCEPTION(
            ReportErrorCode.CANNOT_REPORT_OWN_COMMENT_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "자신이 작성한 댓글은 신고할 수 없습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    ReportExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ReportExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(ReportExceptionTranslator.values())
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
