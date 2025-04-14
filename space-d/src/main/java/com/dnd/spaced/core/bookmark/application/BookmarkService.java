package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.mapper.BookmarkApplicationMapper;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.exception.AlreadyExistsBookmarkException;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
import com.dnd.spaced.core.bookmark.domain.Bookmark;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountDecrementedEvent;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountIncrementedEvent;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final WordRepository wordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createBookmark(Long accountId, CreateBookmarkRequest request) {
        validateWordId(request);
        validateExistsBookmark(accountId, request);

        Bookmark bookmark = new Bookmark(accountId, request.wordId());

        bookmarkRepository.save(bookmark);
        publishAddedBookmarkEvent(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long accountId, DeleteBookmarkRequest request) {
        bookmarkRepository.delete(accountId, request.wordId());
        publishDeletedBookmarkEvent(request.wordId());
    }

    public BookmarkCollectionResponse readBookmarks(
            Long accountId,
            ReadAllBookmarkRequest request,
            Pageable pageable
    ) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllBy(accountId, request.lastBookmarkId(), pageable);

        return BookmarkApplicationMapper.toDto(bookmarks);
    }

    private void validateWordId(CreateBookmarkRequest request) {
        if (isExistsWord(request.wordId())) {
            throw new WordNotFoundException("지정한 식별자의 용어를 찾지 못했습니다.");
        }
    }

    private boolean isExistsWord(Long wordId) {
        return !wordRepository.existsBy(wordId);
    }

    private void validateExistsBookmark(Long accountId, CreateBookmarkRequest request) {
        if (isExistsBookmark(accountId, request.wordId())) {
            throw new AlreadyExistsBookmarkException("이미 북마크에 추가된 용어입니다.");
        }
    }

    private boolean isExistsBookmark(Long accountId, Long wordId) {
        return bookmarkRepository.existsBy(accountId, wordId);
    }

    private void publishAddedBookmarkEvent(Bookmark bookmark) {
        eventPublisher.publishEvent(new WordBookmarkCountIncrementedEvent(bookmark.getWordId()));
    }

    private void publishDeletedBookmarkEvent(Long wordId) {
        eventPublisher.publishEvent(new WordBookmarkCountDecrementedEvent(wordId));
    }
}
