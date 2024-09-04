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
        Account account = findAuthorizedAccount(accountId);

        accountRepository.delete(account);
    }

    @Transactional
    public void changeCareerInfo(String accountId, String jobGroupName, String companyName, String experienceName) {
        Account account = findAuthorizedAccount(accountId);

        account.changeCareerInfo(jobGroupName, companyName, experienceName);
    }

    @Transactional
    public void changeProfileInfo(String accountId, String originalNickname, String profileImageKoreanName) {
        Account account = findAuthorizedAccount(accountId);
        ProfileImageName profileImageName = ProfileImageName.findBy(profileImageKoreanName);

        account.changeProfileInfo(originalNickname, profileImageName.getImageName());
    }

    public AccountInfoDto findAccountInfo(String accountId) {
        Account account = findAuthorizedAccount(accountId);

        return AccountInfoDto.from(account);
    }

    private Account findAuthorizedAccount(String accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenAccountException("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다."));
    }
}
