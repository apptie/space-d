package com.dnd.spaced.config.docs.snippet.exceptions.like;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record LikeExceptionDocs(Map<String, ExceptionContent> processLikeException) {
}
