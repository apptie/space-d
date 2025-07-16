package com.dnd.spaced.core.bookmark.presentation;

import com.dnd.spaced.core.bookmark.application.BookmarkServiceFacade;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.DeleteBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import com.dnd.spaced.global.resolver.bookmark.BookmarkPageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkServiceFacade bookmarkServiceFacade;

    @GetMapping
    public ResponseEntity<BookmarkCollectionResponse> readBookmarks(
            @CurrentAccount AuthAccountId accountId,
            ReadAllBookmarkRequest request,
            @BookmarkPageable Pageable pageable
    ) {
        BookmarkCollectionResponse response = bookmarkServiceFacade.readBookmarks(accountId.id(), request, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody CreateBookmarkRequest request
    ) {
        bookmarkServiceFacade.createBookmark(accountId.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody DeleteBookmarkRequest request
    ) {
        bookmarkServiceFacade.deleteBookmark(accountId.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }
}
