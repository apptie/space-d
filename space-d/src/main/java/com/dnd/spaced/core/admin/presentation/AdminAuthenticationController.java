package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.dto.request.UpdateBlacklistTokenRequest;
import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {

    private final BlacklistTokenService blacklistTokenService;

    @PostMapping("/blacklist-token")
    public ResponseEntity<Void> registerBlacklistToken(@Valid @RequestBody UpdateBlacklistTokenRequest request) {
        blacklistTokenService.register(request.accountId());

        return ResponseEntityConst.NO_CONTENT;
    }
}
