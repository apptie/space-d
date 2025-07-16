package com.dnd.spaced.core.bookmark.application;

import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.domain.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class DeleteBookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void deleteBookmark(Long accountId, DeleteBookmarkRequest request) {
        bookmarkRepository.delete(accountId, request.wordId());
    }
}
