package com.dnd.spaced.config.docs.snippet.exceptions.todayquiz;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.QuizErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/today-quizzes")
public class TodayQuizExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<TodayQuizExceptionDocs>> findExceptions() {
        TodayQuizExceptionDocs todayQuizExceptionDocs =
                TodayQuizExceptionDocs.builder()
                                      .readLatestTodayQuizException(calculateReadLatestTodayQuizException())
                                      .readTodayQuizByException(calculateReadTodayQuizByException())
                                      .gradeTodayQuizException(calculateGradeTodayQuizException())
                                      .readGradedAnswerAllException(calculateReadGradedAnswerAllException())
                                      .readGradedAnswerAllByTodayQuizIdException(calculatereadGradedAnswerAllByTodayQuizIdException())
                                      .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(todayQuizExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateReadLatestTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadTodayQuizByException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateGradeTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "answer");
        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_SUBMITTED_TODAY_QUIZ_OPTION_INDEX_EXCEPTION,
                QuizErrorCode.ALREADY_GRADE_TODAY_QUIZ_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadGradedAnswerAllException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        processQuizException(
                exceptionContent,
                QuizErrorCode.TODAY_QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculatereadGradedAnswerAllByTodayQuizIdException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }
}
