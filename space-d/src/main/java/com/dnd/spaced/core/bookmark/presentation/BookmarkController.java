package com.dnd.spaced.core.bookmark.presentation;

import com.dnd.spaced.core.bookmark.application.BookmarkService;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
import com.dnd.spaced.global.auth.resolver.CurrentAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import com.dnd.spaced.global.resolver.bookmark.BookmarkPageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<BookmarkCollectionResponse> readBookmarks(
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            ReadAllBookmarkRequest request,
            @BookmarkPageable Pageable pageable
    ) {
        BookmarkCollectionResponse response = bookmarkService.readBookmarks(accountInfo.accountId(), request, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            @Valid @RequestBody CreateBookmarkRequest request
    ) {
        bookmarkService.createBookmark(accountInfo.accountId(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            @PathVariable Long bookmarkId
    ) {
        bookmarkService.deleteBookmark(accountInfo.accountId(), bookmarkId);

        return ResponseEntityConst.NO_CONTENT;
    }
}
