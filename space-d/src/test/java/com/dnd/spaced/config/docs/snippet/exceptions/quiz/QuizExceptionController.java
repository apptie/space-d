package com.dnd.spaced.config.docs.snippet.exceptions.quiz;

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
@RequestMapping("/test/quizzes")
public class QuizExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<QuizExceptionDocs>> findExceptions() {
        QuizExceptionDocs quizExceptionDocs =
                QuizExceptionDocs.builder()
                                 .createQuizException(calculateCreateQuizException())
                                 .readQuizzesException(calculateReadQuizzesException())
                                 .gradeQuizException(calculateGradeQuizException())
                                 .readTargetQuizGradedAnswers(calculateReadTargetQuizGradedAnswersException())
                                 .readQuizGradedAnswers(calculateReadQuizGradedAnswersException())
                                 .readQuizException(calculateReadQuizException())
                                 .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(quizExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateCreateQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.INVALID_QUIZ_CATEGORY_NAME_EXCEPTION,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_QUIZ_WORD_COUNT_EXCEPTION,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateGradeQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "answers");
        processQuizException(
                exceptionContent,
                QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION,
                QuizErrorCode.ALREADY_GRADE_QUIZ_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadTargetQuizGradedAnswersException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadQuizGradedAnswersException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.QUIZ_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadQuizzesException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }
}
