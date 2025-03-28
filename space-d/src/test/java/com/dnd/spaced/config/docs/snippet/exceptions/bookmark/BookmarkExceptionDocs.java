package com.dnd.spaced.config.docs.snippet.exceptions.bookmark;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record BookmarkExceptionDocs(
        Map<String, ExceptionContent> createBookmarkException,
        Map<String, ExceptionContent> deleteBookmarkException,
        Map<String, ExceptionContent> readBookmarksException
) {
}
