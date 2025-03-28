package com.dnd.spaced.config.docs.snippet.exceptions.localimage;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record LocalImageExceptionDocs(Map<String, ExceptionContent> readLocalImageException) {
}
