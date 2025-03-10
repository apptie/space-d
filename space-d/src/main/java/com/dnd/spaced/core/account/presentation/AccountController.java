package com.dnd.spaced.core.account.presentation;

import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerInfoRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileInfoRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AccountInfo;
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
    public ResponseEntity<Void> withdrawal(@AuthAccount AccountInfo accountInfo) {
        accountService.withdrawal(accountInfo.id());

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/career-info")
    public ResponseEntity<Void> changeCareerInfo(
            @AuthAccount AccountInfo accountInfo,
            @Valid @RequestBody ChangeCareerInfoRequest request
    ) {
        accountService.changeCareerInfo(accountInfo.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/profile-info")
    public ResponseEntity<Void> changeProfileInfo(
            @AuthAccount AccountInfo accountInfo,
            @Valid @RequestBody ChangeProfileInfoRequest request
    ) {
        accountService.changeProfileInfo(accountInfo.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping
    public ResponseEntity<AccountResponse> readAccount(@AuthAccount AccountInfo accountInfo) {
        AccountResponse response = accountService.readAccount(accountInfo.id());

        return ResponseEntity.ok(response);
    }
}
