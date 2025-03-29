package com.dnd.spaced.config.docs.snippet.exceptions.report;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.ReportErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/reports")
public class ReportExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<ReportExceptionDocs>> findExceptions() {
        ReportExceptionDocs reportExceptionDocs =
                ReportExceptionDocs.builder()
                                   .reportException(calculateReportException())
                                   .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(reportExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateReportException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        putMethodArgumentNotValidExceptionContent(exceptionContent, "commentId", "cause");
        processReportException(
                exceptionContent,
                ReportErrorCode.CANNOT_REPORT_OWN_COMMENT_EXCEPTION,
                ReportErrorCode.REPORT_REASON_NOT_FOUND_EXCEPTION,
                ReportErrorCode.COMMENT_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }
}
