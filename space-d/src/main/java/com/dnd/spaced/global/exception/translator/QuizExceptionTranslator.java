package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.code.ErrorCode;
import com.dnd.spaced.global.exception.code.QuizErrorCode;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QuizExceptionTranslator implements ExceptionTranslator {
    INVALID_QUIZ_CATEGORY_NAME_EXCEPTION(
            QuizErrorCode.INVALID_QUIZ_CATEGORY_NAME_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "잘못된 퀴즈 카테고리 이름입니다."
    ),
    INVALID_QUIZ_QUESTION_CONTENT_EXCEPTION(
            QuizErrorCode.INVALID_QUIZ_QUESTION_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "퀴즈 생성에 실패했습니다."
    ),
    INVALID_QUIZ_QUESTION_EXAMPLE_EXCEPTION(
            QuizErrorCode.INVALID_QUIZ_QUESTION_CONTENT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "퀴즈 생성에 실패했습니다."
    ),
    INVALID_QUIZ_OPTION_CONTENT_EXCEPTION(
            QuizErrorCode.INVALID_QUIZ_OPTION_CONTENT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "퀴즈 생성에 실패했습니다."
    ),
    INVALID_SUBMITTED_QUIZ_OPTION_INDEX_EXCEPTION(
            QuizErrorCode.INVALID_SUBMITTED_QUIZ_OPTION_INDEX_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "없는 보기를 선택했습니다."
    ),
    INVALID_TODAY_QUIZ_OPTION_CONTENT_EXCEPTION(
            QuizErrorCode.INVALID_TODAY_QUIZ_OPTION_CONTENT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "오늘의 퀴즈 생성에 실패했습니다."
    ),
    INVALID_TODAY_QUIZ_QUESTION_EXCEPTION(
            QuizErrorCode.INVALID_TODAY_QUIZ_QUESTION_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "오늘의 퀴즈 생성에 실패했습니다."
    ),
    INVALID_TODAY_QUIZ_QUESTION_CONTENT_EXCEPTION(
            QuizErrorCode.INVALID_TODAY_QUIZ_QUESTION_CONTENT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "오늘의 퀴즈 생성에 실패했습니다."
    ),
    INVALID_SUBMITTED_TODAY_QUIZ_OPTION_INDEX_EXCEPTION(
            QuizErrorCode.INVALID_SUBMITTED_TODAY_QUIZ_OPTION_INDEX_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "없는 보기를 선택했습니다."
    ),
    QUIZ_CATEGORY_NOT_FOUND_EXCEPTION(
            QuizErrorCode.QUIZ_CATEGORY_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "없는 퀴즈 카테고리를 입력했습니다."
    ),
    INVALID_QUIZ_WORD_COUNT_EXCEPTION(
            QuizErrorCode.INVALID_QUIZ_WORD_COUNT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "퀴즈 생성에 실패했습니다."
    ),
    INVALID_TODAY_QUIZ_WORD_COUNT_EXCEPTION(
            QuizErrorCode.INVALID_TODAY_QUIZ_WORD_COUNT_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "오늘의 퀴즈 생성에 실패했습니다."
    ),
    QUIZ_NOT_FOUND_EXCEPTION(
            QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "퀴즈를 찾지 못했습니다."
    ),
    TODAY_QUIZ_NOT_FOUND_EXCEPTION(
            QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "오늘의 퀴즈를 찾지 못했습니다."
    ),
    INVALID_SUBMIT_ANSWERS_COUNT_EXCEPTION(
            QuizErrorCode.INVALID_SUBMIT_ANSWERS_COUNT_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "문제 개수와 제출한 정답 개수가 다릅니다."
    ),
    WORD_METADATA_NOT_FOUND_EXCEPTION(
            QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "용어 메타데이터가 정상적으로 초기화되지 않았습니다."
    ),
    ALREADY_GRADE_QUIZ_EXCEPTION(
            QuizErrorCode.ALREADY_GRADE_QUIZ_EXCEPTION,
            HttpStatus.BAD_REQUEST,
            "이미 푼 퀴즈입니다."
    );

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    QuizExceptionTranslator(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static QuizExceptionTranslator findBy(ErrorCode errorCode) {
        return Arrays.stream(QuizExceptionTranslator.values())
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
