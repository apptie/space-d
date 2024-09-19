package com.dnd.spaced.core.like.presentation;

import com.dnd.spaced.core.like.application.LikeService;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<Void> processLike(@AuthAccount AuthAccountInfo accountInfo, @PathVariable Long commentId) {
        likeService.processLike(accountInfo.id(), commentId);

        return ResponseEntityConst.NO_CONTENT;
    }
}
