package com.dnd.spaced.config.docs.snippet.exceptions.localimage;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.ImageErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/local-images")
public class LocalImageExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<LocalImageExceptionDocs>> findExceptions() {
        LocalImageExceptionDocs localImageExceptionDocs =
                LocalImageExceptionDocs.builder()
                                       .readLocalImageException(calculateLocalImageNotFoundException())
                                       .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(localImageExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateLocalImageNotFoundException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        processImageException(exceptionContent, ImageErrorCode.IMAGE_FILE_NOT_FOUND_EXCEPTION);

        return exceptionContent;
    }
}
