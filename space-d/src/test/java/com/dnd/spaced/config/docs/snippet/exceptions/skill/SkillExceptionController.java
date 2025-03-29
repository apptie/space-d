package com.dnd.spaced.config.docs.snippet.exceptions.skill;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.SkillErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/skills")
public class SkillExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<SkillExceptionDocs>> findExceptions() {
        SkillExceptionDocs exceptionDocs =
                SkillExceptionDocs.builder()
                             .readSkillException(calculateReadSkillException())
                             .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(exceptionDocs));
    }

    private Map<String, ExceptionContent> calculateReadSkillException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processSkillException(
                exceptionContent,
                SkillErrorCode.QUIZ_METADATA_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }
}
