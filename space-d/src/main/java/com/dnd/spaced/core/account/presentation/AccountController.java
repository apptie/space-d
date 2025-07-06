package com.dnd.spaced.core.account.presentation;

import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.global.auth.resolver.CurrentAccountInfo;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
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
    public ResponseEntity<Void> withdrawal(@CurrentAccountInfo AuthAccountInfo accountInfo) {
        accountService.withdrawal(accountInfo.accountId());

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/career-info")
    public ResponseEntity<Void> changeCareerInfo(
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            @Valid @RequestBody ChangeCareerRequest request
    ) {
        accountService.changeCareerInfo(accountInfo.accountId(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/profile-info")
    public ResponseEntity<Void> changeProfileInfo(
            @CurrentAccountInfo AuthAccountInfo accountInfo,
            @Valid @RequestBody ChangeProfileRequest request
    ) {
        accountService.changeProfileInfo(accountInfo.accountId(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping
    public ResponseEntity<AccountResponse> readAccount(@CurrentAccountInfo AuthAccountInfo accountInfo) {
        AccountResponse response = accountService.readAccount(accountInfo.accountId());

        return ResponseEntity.ok(response);
    }
}
