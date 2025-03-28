package com.dnd.spaced.config.docs.snippet.exceptions.word;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record WordExceptionDocs(Map<String, ExceptionContent> readWordException) {
}
