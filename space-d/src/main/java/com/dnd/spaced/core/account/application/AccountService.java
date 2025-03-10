package com.dnd.spaced.core.account.application;

import com.dnd.spaced.core.account.application.dto.mapper.AccountApplicationMapper;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerInfoRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileInfoRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
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
    public void withdrawal(Long accountId) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        authorizedAccount.withdrawal();
    }

    @Transactional
    public void changeCareerInfo(Long accountId, ChangeCareerInfoRequest request) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        authorizedAccount.changeCareerInfo(
                request.changedJobGroupName(),
                request.changedCompanyName(),
                request.changedExperienceName()
        );
    }

    @Transactional
    public void changeProfileInfo(Long accountId, ChangeProfileInfoRequest request) {
        Account authorizedAccount = findAuthorizedAccount(accountId);
        ProfileImageName changedProfileImageName = ProfileImageName.findBy(request.changedProfileImageKoreanName());

        authorizedAccount.changeProfileInfo(request.changedNickname(), changedProfileImageName.getImageName());
    }

    public AccountResponse findAccountInfo(Long accountId) {
        Account authorizedAccount = findAuthorizedAccount(accountId);

        return AccountApplicationMapper.toDto(authorizedAccount);
    }

    private Account findAuthorizedAccount(Long accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenAccountException("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다."));
    }
}
