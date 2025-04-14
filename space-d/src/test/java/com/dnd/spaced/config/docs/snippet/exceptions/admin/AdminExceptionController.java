package com.dnd.spaced.config.docs.snippet.exceptions.admin;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.QuizErrorCode;
import com.dnd.spaced.global.exception.code.WordErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/admin")
public class AdminExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<AdminExceptionDocs>> findExceptions() {
        AdminExceptionDocs adminExceptionDocs =
                AdminExceptionDocs.builder()
                                  .createWordException(calculateCreateWordException())
                                  .updateWordExampleException(calculateUpdateWordExampleException())
                                  .deleteWordExampleException(calculateDeleteWordExampleException())
                                  .deletePronunciationException(calculateDeletePronunciationException())
                                  .processReportException(calculateProcessReportException())
                                  .createTodayQuizException(calculateCreateTodayQuizException())
                                  .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(adminExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateCreateWordException() {
        Map<String, ExceptionContent> saveWordException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(saveWordException);
        putForbiddenExceptionContent(saveWordException);
        putMethodArgumentNotValidExceptionContent(
                saveWordException,
                "name",
                "meaning",
                "categoryName",
                "pronunciations",
                "pronunciation",
                "typeName",
                "examples"
        );
        processWordException(saveWordException, WordErrorCode.values());

        return saveWordException;
    }

    private Map<String, ExceptionContent> calculateUpdateWordExampleException() {
        Map<String, ExceptionContent> updateWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(updateWordExampleException);
        putForbiddenExceptionContent(updateWordExampleException);
        putMethodArgumentNotValidExceptionContent(updateWordExampleException, "content");
        processWordException(
                updateWordExampleException,
                WordErrorCode.WORD_EXAMPLE_NOT_FOUND_EXCEPTION,
                WordErrorCode.INVALID_WORD_EXAMPLE_CONTENT_EXCEPTION
        );

        return updateWordExampleException;
    }

    private Map<String, ExceptionContent> calculateDeleteWordExampleException() {
        Map<String, ExceptionContent> deleteWordExampleException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(deleteWordExampleException);
        putForbiddenExceptionContent(deleteWordExampleException);
        processWordException(
                deleteWordExampleException,
                WordErrorCode.WORD_EXAMPLE_DELETION_NOT_ALLOWED,
                WordErrorCode.WORD_EXAMPLE_NOT_FOUND_EXCEPTION
        );

        return deleteWordExampleException;
    }

    private Map<String, ExceptionContent> calculateDeletePronunciationException() {
        Map<String, ExceptionContent> deletePronunciationException = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(deletePronunciationException);
        putForbiddenExceptionContent(deletePronunciationException);
        processWordException(
                deletePronunciationException,
                WordErrorCode.PRONUNCIATION_DELETION_NOT_ALLOWED,
                WordErrorCode.PRONUNCIATION_NOT_FOUND_EXCEPTION
        );

        return deletePronunciationException;
    }

    private Map<String, ExceptionContent> calculateProcessReportException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateCreateTodayQuizException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putForbiddenExceptionContent(exceptionContent);
        processQuizException(
                exceptionContent,
                QuizErrorCode.WORD_METADATA_NOT_FOUND_EXCEPTION,
                QuizErrorCode.INVALID_TODAY_QUIZ_WORD_COUNT_EXCEPTION
        );

        return exceptionContent;
    }
}
