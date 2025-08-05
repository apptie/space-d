package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.mapper.BookmarkMapper;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadBookmarkService {

    private final BookmarkMapper mapper;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkCollectionResponse readBookmarks(
            Long accountId,
            ReadAllBookmarkRequest request,
            Pageable pageable
    ) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllBy(accountId, request.lastBookmarkId(), pageable);

        return mapper.toDto(bookmarks);
    }
}
