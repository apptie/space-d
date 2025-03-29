package com.dnd.spaced.config.docs.snippet.exceptions.bookmark;

import com.dnd.spaced.config.docs.snippet.CommonExceptionController;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.global.exception.code.BookmarkErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/bookmarks")
public class BookmarkExceptionController extends CommonExceptionController {

    @GetMapping("/exceptions")
    public ResponseEntity<CommonDocsResponse<BookmarkExceptionDocs>> findExceptions() {
        BookmarkExceptionDocs bookmarkExceptionDocs =
                BookmarkExceptionDocs.builder()
                                     .createBookmarkException(calculateCreateBookmarkException())
                                     .deleteBookmarkException(calculateDeleteBookmarkException())
                                     .readBookmarksException(calculateReadBookmarksException())
                                     .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(bookmarkExceptionDocs));
    }

    private Map<String, ExceptionContent> calculateCreateBookmarkException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processBookmarkException(
                exceptionContent,
                BookmarkErrorCode.WORD_NOT_FOUND_EXCEPTION,
                BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateDeleteBookmarkException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);
        processBookmarkException(
                exceptionContent,
                BookmarkErrorCode.BOOKMARK_NOT_FOUND_EXCEPTION
        );

        return exceptionContent;
    }

    private Map<String, ExceptionContent> calculateReadBookmarksException() {
        Map<String, ExceptionContent> exceptionContent = new LinkedHashMap<>();

        putUnauthorizedExceptionContent(exceptionContent);

        return exceptionContent;
    }
}
