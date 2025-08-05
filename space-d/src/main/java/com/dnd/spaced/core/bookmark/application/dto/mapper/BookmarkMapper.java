package com.dnd.spaced.core.bookmark.application.dto.mapper;

import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse.BookmarkResponse;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.global.mapper.Mapper;
import java.util.List;

@Mapper
public class BookmarkMapper {

    public BookmarkCollectionResponse toDto(List<Bookmark> bookmarks) {
        if (bookmarks.isEmpty()) {
            return new BookmarkCollectionResponse(List.of(), null);
        }

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
                                                            .map(this::toBookmarkResponse)
                                                            .toList();
        return new BookmarkCollectionResponse(bookmarkResponses, bookmarks.get(bookmarks.size() - 1).getId());
    }

    private BookmarkResponse toBookmarkResponse(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getAccountId(),
                bookmark.getWordId(),
                bookmark.getCreatedAt()
        );
    }
}
