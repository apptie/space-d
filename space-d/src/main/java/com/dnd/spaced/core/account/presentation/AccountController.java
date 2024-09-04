package com.dnd.spaced.core.account.presentation;

import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateCareerInfoRequest;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateProfileInfoRequest;
import com.dnd.spaced.core.account.presentation.dto.response.AccountInfoResponse;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @DeleteMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal(@AuthAccount AuthAccountInfo accountInfo) {
        accountService.withdrawal(accountInfo.id());

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/career-info")
    public ResponseEntity<Void> changeCareerInfo(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody UpdateCareerInfoRequest request
    ) {
        accountService.changeCareerInfo(
                accountInfo.id(),
                request.jobGroupName(),
                request.companyName(),
                request.experienceName()
        );

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/profile-info")
    public ResponseEntity<Void> changeProfileInfo(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody UpdateProfileInfoRequest request
    ) {
        accountService.changeProfileInfo(accountInfo.id(), request.originNickname(), request.profileImageKoreanName());

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping
    public ResponseEntity<Object> findAccountInfo(@AuthAccount AuthAccountInfo accountInfo) {
        AccountInfoDto result = accountService.findAccountInfo(accountInfo.id());

        return ResponseEntity.ok(AccountInfoResponse.from(result));
    }
}
