package com.dnd.spaced.core.comment.presentation;

import com.dnd.spaced.core.comment.application.CommentService;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.core.comment.application.dto.request.ReadAllCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import com.dnd.spaced.global.resolver.comment.CommentPageable;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/words/{wordId}/comments")
    public ResponseEntity<Void> save(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody CreateCommentRequest request,
            @PathVariable Long wordId
    ) {
        commentService.create(accountInfo.id(), wordId, request);

        URI location = UriComponentsBuilder.fromPath("/words/{wordId}")
                                           .buildAndExpand(wordId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@AuthAccount AuthAccountInfo accountInfo, @PathVariable Long commentId) {
        commentService.delete(accountInfo.id(), commentId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> update(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody UpdateCommentRequest request,
            @PathVariable Long commentId
    ) {
        commentService.update(accountInfo.id(), commentId, request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping("/words/{wordId}/comments")
    public ResponseEntity<CommentCollectionResponse> readAllBy(
            @AuthAccount(required = false) AuthAccountInfo accountInfo,
            @PathVariable Long wordId,
            ReadAllCommentRequest request,
            @CommentPageable Pageable pageable
    ) {
        CommentCollectionResponse response = commentService.readAllBy(
                accountInfo.id(),
                wordId,
                request.lastCommentId(),
                pageable
        );

        return ResponseEntity.ok(response);
    }
}
