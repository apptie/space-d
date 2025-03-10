package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.mapper.BookmarkApplicationMapper;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.exception.BookmarkNotFoundException;
import com.dnd.spaced.core.bookmark.application.exception.ForbiddenDeleteBookmarkException;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final WordRepository wordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createBookmark(Long accountId, CreateBookmarkRequest request) {
        validateWordId(request);

        Bookmark bookmark = new Bookmark(accountId, request.wordId());

        bookmarkRepository.save(bookmark);
        publishAddedBookmarkEvent(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long accountId, Long bookmarkId) {
        Bookmark bookmark = findBookmark(bookmarkId);

        validateBookmarkCreator(accountId, bookmark);

        bookmarkRepository.delete(bookmark);
        publishDeletedBookmarkEvent(bookmark);
    }

    public BookmarkCollectionResponse readBookmarks(
            Long accountId,
            ReadAllBookmarkRequest request,
            Pageable pageable
    ) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllBy(accountId, request.lastBookmarkId(), pageable);

        return BookmarkApplicationMapper.toDto(bookmarks);
    }

    private void validateBookmarkCreator(Long accountId, Bookmark bookmark) {
        if (bookmark.isNotCreator(accountId)) {
            throw new ForbiddenDeleteBookmarkException("북마크 삭제는 생성자만이 가능합니다.");
        }
    }

    private void validateWordId(CreateBookmarkRequest request) {
        if (!wordRepository.existsBy(request.wordId())) {
            throw new WordNotFoundException("지정한 식별자의 용어를 찾지 못했습니다.");
        }
    }

    private Bookmark findBookmark(Long bookmarkId) {
        return bookmarkRepository.findBy(bookmarkId)
                                 .orElseThrow(
                                         () -> new BookmarkNotFoundException(
                                                 "지정한 식별자의 북마크를 찾지 못했습니다."
                                         )
                                 );
    }

    private void publishAddedBookmarkEvent(Bookmark bookmark) {
        eventPublisher.publishEvent(new WordBookmarkCountIncrementedEvent(bookmark.getId()));
    }

    private void publishDeletedBookmarkEvent(Bookmark bookmark) {
        eventPublisher.publishEvent(new WordBookmarkCountDecrementedEvent(bookmark.getId()));
    }
}
