package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountDecrementedEvent;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountIncrementedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceFacade {

    private final CreateBookmarkService createBookmarkService;
    private final ReadBookmarkService readBookmarkService;
    private final DeleteBookmarkService deleteBookmarkService;
    private final ApplicationEventPublisher eventPublisher;

    public void createBookmark(Long accountId, CreateBookmarkRequest request) {
        Bookmark bookmark = createBookmarkService.createBookmark(accountId, request);

        publishAddedBookmarkEvent(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long accountId, DeleteBookmarkRequest request) {
        deleteBookmarkService.deleteBookmark(accountId, request);
        publishDeletedBookmarkEvent(request.wordId());
    }

    public BookmarkCollectionResponse readBookmarks(
            Long accountId,
            ReadAllBookmarkRequest request,
            Pageable pageable
    ) {
        return readBookmarkService.readBookmarks(accountId, request, pageable);
    }

    private void publishAddedBookmarkEvent(Bookmark bookmark) {
        eventPublisher.publishEvent(new WordBookmarkCountIncrementedEvent(bookmark.getWordId()));
    }

    private void publishDeletedBookmarkEvent(Long wordId) {
        eventPublisher.publishEvent(new WordBookmarkCountDecrementedEvent(wordId));
    }
}
