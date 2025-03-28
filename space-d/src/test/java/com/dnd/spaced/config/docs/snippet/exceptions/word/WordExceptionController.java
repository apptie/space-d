package com.dnd.spaced.config.docs.snippet.exceptions.word;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.WordErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/words")
public class WordExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<WordExceptionDocs>> findExceptions() {
        WordExceptionDocs wordExceptionDocs =
                WordExceptionDocs.builder()
                                 .readWordException(calculateReadWordException())
                                 .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(wordExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateReadWordException() {
        Map<String, ExceptionContent> readWordException = new LinkedHashMap<>();

        processWordException(readWordException, WordErrorCode.WORD_NOT_FOUND);

        return readWordException;
    }
}
