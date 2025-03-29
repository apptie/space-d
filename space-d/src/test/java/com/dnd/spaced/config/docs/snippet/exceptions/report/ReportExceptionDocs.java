package com.dnd.spaced.config.docs.snippet.exceptions.report;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record ReportExceptionDocs(Map<String, ExceptionContent> reportException) {
}
