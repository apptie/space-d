package com.dnd.spaced.core.like.presentation;

import com.dnd.spaced.core.like.application.LikeService;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments/{commentId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> processLike(
            @CurrentAccount AuthAccountId accountId,
            @PathVariable Long commentId
    ) {
        likeService.processLike(accountId.id(), commentId);

        return ResponseEntityConst.NO_CONTENT;
    }
}
