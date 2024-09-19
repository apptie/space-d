package com.dnd.spaced.core.comment.presentation;

import com.dnd.spaced.core.comment.application.CommentService;
import com.dnd.spaced.core.comment.application.dto.response.ReadAllCommentDto;
import com.dnd.spaced.core.comment.presentation.dto.request.ReadAllCommentRequest;
import com.dnd.spaced.core.comment.presentation.dto.request.SaveCommentRequest;
import com.dnd.spaced.core.comment.presentation.dto.request.UpdateCommentRequest;
import com.dnd.spaced.core.comment.presentation.dto.response.ReadAllCommentResponse;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import com.dnd.spaced.global.resolver.comment.CommonCommentPageable;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/words/{wordId}/comments")
    public ResponseEntity<Void> save(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody SaveCommentRequest request,
            @PathVariable Long wordId
    ) {
        commentService.save(accountInfo.id(), wordId, request.content());

        return ResponseEntity.created(URI.create("/words/" + wordId))
                             .build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@AuthAccount AuthAccountInfo accountInfo, @PathVariable Long id) {
        commentService.delete(accountInfo.id(), id);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> update(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody UpdateCommentRequest request,
            @PathVariable Long id
    ) {
        commentService.update(accountInfo.id(), id, request.content());

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping("/words/{wordId}/comments")
    public ResponseEntity<ReadAllCommentResponse> readAlLBy(
            @AuthAccount(required = false) AuthAccountInfo accountInfo,
            @PathVariable Long wordId,
            ReadAllCommentRequest request,
            @CommonCommentPageable Pageable pageable
    ) {
        List<ReadAllCommentDto> result = commentService.readAllBy(
                accountInfo.id(),
                wordId,
                request.lastCommentId(),
                pageable
        );

        return ResponseEntity.ok(ReadAllCommentResponse.from(result));
    }
}
