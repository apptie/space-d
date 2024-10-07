package com.dnd.spaced.core.account.application;

import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.ProfileImageName;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void withdrawal(String accountId) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        accountRepository.delete(authorizedAccount);
    }

    @Transactional
    public void changeCareerInfo(String accountId, String jobGroupName, String companyName, String experienceName) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        authorizedAccount.changeCareerInfo(jobGroupName, companyName, experienceName);
    }

    @Transactional
    public void changeProfileInfo(String accountId, String originalNickname, String profileImageKoreanName) {
        Account authorizedAccount = findAuthorizedAccount(accountId);
        ProfileImageName profileImageName = ProfileImageName.findBy(profileImageKoreanName);

        authorizedAccount.changeProfileInfo(originalNickname, profileImageName.getImageName());
    }

    public AccountInfoDto findAccountInfo(String accountId) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        return AccountInfoDto.from(authorizedAccount);
    }

    private Account findAuthorizedAccount(String accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenAccountException("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다."));
    }
}
