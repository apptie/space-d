package com.dnd.spaced.core.comment.presentation;

import com.dnd.spaced.core.comment.application.CommentService;
import com.dnd.spaced.core.comment.application.dto.request.CreateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.ReadAllCommentRequest;
import com.dnd.spaced.core.comment.application.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.application.dto.response.CommentCollectionResponse;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.auth.resolver.GuestAccountId;
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
    public ResponseEntity<Void> creteComment(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody CreateCommentRequest request,
            @PathVariable Long wordId
    ) {
        commentService.createComment(accountId.id(), wordId, request);

        URI location = UriComponentsBuilder.fromPath("/words/{wordId}")
                                           .buildAndExpand(wordId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@CurrentAccount AuthAccountId accountId, @PathVariable Long commentId) {
        commentService.deleteComment(accountId.id(), commentId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> update(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody UpdateCommentRequest request,
            @PathVariable Long commentId
    ) {
        commentService.updateComment(accountId.id(), commentId, request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping("/words/{wordId}/comments")
    public ResponseEntity<CommentCollectionResponse> readComments(
            @CurrentAccount GuestAccountId accountId,
            @PathVariable Long wordId,
            ReadAllCommentRequest request,
            @CommentPageable Pageable pageable
    ) {
        CommentCollectionResponse response = commentService.readComments(
                accountId.id(),
                wordId,
                request.lastCommentId(),
                pageable
        );

        return ResponseEntity.ok(response);
    }
}
