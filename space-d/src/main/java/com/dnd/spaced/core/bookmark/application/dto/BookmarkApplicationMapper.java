package com.dnd.spaced.core.bookmark.application.dto;

import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse.BookmarkResponse;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookmarkApplicationMapper {

    public static BookmarkCollectionResponse toDto(List<Bookmark> bookmarks) {
        if (bookmarks.isEmpty()) {
            return new BookmarkCollectionResponse(List.of(), null);
        }

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
                                                            .map(BookmarkApplicationMapper::toBookmarkResponse)
                                                            .toList();
        return new BookmarkCollectionResponse(bookmarkResponses, bookmarks.get(bookmarks.size() - 1).getId());
    }

    private static BookmarkResponse toBookmarkResponse(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getAccountId(),
                bookmark.getWordId(),
                bookmark.getCreatedAt()
        );
    }
}
