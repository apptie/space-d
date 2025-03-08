package com.dnd.spaced.core.bookmark.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record BookmarkCollectionResponse(List<BookmarkResponse> bookmarks, Long lastBookmarkId) {

    public record BookmarkResponse(Long bookmarkId, Long accountId, Long wordId, LocalDateTime createdAt) {
    }
}
