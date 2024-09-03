package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.AccountErrorCode;
import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountExceptionTranslator implements ExceptionTranslator {
    INVALID_COMPANY_EXCEPTION(
            AccountErrorCode.INVALID_COMPANY,
            HttpStatus.BAD_REQUEST,
            "잘못된 회사 종류를 입력했습니다."
    ),
    INVALID_EXPERIENCE_EXCEPTION(
            AccountErrorCode.INVALID_EXPERIENCE,
            HttpStatus.BAD_REQUEST,
            "잘못된 경력을 입력했습니다."
    ),
    INVALID_ID_EXCEPTION(
            AccountErrorCode.INVALID_ID,
            HttpStatus.BAD_REQUEST,
            "잘못된 ID를 입력했습니다."
    ),
    INVALID_JOB_GROUP_EXCEPTION(
            AccountErrorCode.INVALID_JOB_GROUP,
            HttpStatus.BAD_REQUEST,
            "잘못된 직군을 입력했습니다."
    ),
    INVALID_NICKNAME_EXCEPTION(
            AccountErrorCode.INVALID_NICKNAME,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "회원 정보 초기화 중 닉네임과 관련된 문제가 발생했습니다."
    ),
    INVALID_NICKNAME_METADATA_EXCEPTION(
            AccountErrorCode.INVALID_NICKNAME_METADATA,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "회원 정보 초기화 중 닉네임과 관련된 문제가 발생했습니다."
    ),
    INVALID_PROFILE_IMAGE_EXCEPTION(
            AccountErrorCode.INVALID_PROFILE_IMAGE,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "회원 정보 초기화 중 프로필 이미지와 관련된 문제가 발생했습니다."
    ),
    INVALID_ROLE_NAME_EXCEPTION(
            AccountErrorCode.INVALID_ROLE,
            HttpStatus.BAD_REQUEST,
            "잘못된 권한 이름을 입력했습니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    AccountExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static AccountExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(AccountExceptionTranslator.values())
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
