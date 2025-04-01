package com.dnd.spaced.config.docs.snippet.exceptions.auth;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record TokenExceptionDocs(Map<String, ExceptionContent> commonTokenException) {
}
