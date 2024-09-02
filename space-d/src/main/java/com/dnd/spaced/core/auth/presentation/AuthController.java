package com.dnd.spaced.core.auth.presentation;

import com.dnd.spaced.core.auth.application.InitAccountInfoService;
import com.dnd.spaced.core.auth.presentation.dto.request.UpdateAccountCareerInfoRequest;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthController {

    private final InitAccountInfoService initAccountInfoService;

    @PostMapping("/profile")
    public ResponseEntity<Void> initAccountProfile(
            @AuthAccount AuthAccountInfo accountInfo,
            UpdateAccountCareerInfoRequest request
    ) {
        initAccountInfoService.initCareerInfo(
                accountInfo.id(),
                request.jobGroupName(),
                request.companyName(),
                request.experienceName()
        );

        return ResponseEntityConst.NO_CONTENT;
    }
}
