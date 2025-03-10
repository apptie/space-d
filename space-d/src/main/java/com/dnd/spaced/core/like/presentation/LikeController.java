package com.dnd.spaced.core.like.presentation;

import com.dnd.spaced.core.like.application.LikeService;
import com.dnd.spaced.global.auth.resolver.CurrentAccountInfo;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
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
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            @PathVariable Long commentId
    ) {
        likeService.processLike(accountInfo.accountId(), commentId);

        return ResponseEntityConst.NO_CONTENT;
    }
}
