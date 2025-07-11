package com.dnd.spaced.core.account.presentation;

import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.global.auth.resolver.CurrentAccount;
import com.dnd.spaced.global.auth.resolver.AuthAccountId;
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
    public ResponseEntity<Void> withdrawal(@CurrentAccount AuthAccountId accountId) {
        accountService.withdrawal(accountId.id());

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/career-info")
    public ResponseEntity<Void> changeCareer(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody ChangeCareerRequest request
    ) {
        accountService.changeCareerInfo(accountId.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @PutMapping("/profile-info")
    public ResponseEntity<Void> changeProfile(
            @CurrentAccount AuthAccountId accountId,
            @Valid @RequestBody ChangeProfileRequest request
    ) {
        accountService.changeProfileInfo(accountId.id(), request);

        return ResponseEntityConst.NO_CONTENT;
    }

    @GetMapping
    public ResponseEntity<AccountResponse> readAccount(@CurrentAccount AuthAccountId accountId) {
        AccountResponse response = accountService.readAccount(accountId.id());

        return ResponseEntity.ok(response);
    }
}
