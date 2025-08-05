package com.dnd.spaced.core.account.application;

import com.dnd.spaced.core.account.application.dto.mapper.AccountResponseMapper;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.application.exception.ForbiddenAccountException;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountResponseMapper mapper;

    @Transactional
    public void withdrawal(Long accountId) {
        Account account = findAccount(accountId);

        withdrawAccount(account);
    }

    @Transactional
    public void changeCareer(Long accountId, ChangeCareerRequest request) {
        Account account = findAccount(accountId);

        updateAccountCareer(account, request);
    }

    @Transactional
    public void changeProfile(Long accountId, ChangeProfileRequest request) {
        Account account = findAccount(accountId);
        ProfileImageName changedProfileImageName = findProfileImageName(request);

        updateAccountProfile(account, request, changedProfileImageName);
    }

    public AccountResponse readAccount(Long accountId) {
        Account account = findAccount(accountId);

        return convertAccountResponse(account);
    }

    private void withdrawAccount(Account account) {
        account.withdrawal();
    }

    private ProfileImageName findProfileImageName(ChangeProfileRequest request) {
        return ProfileImageName.findByKorean(request.changedProfileImageKoreanName());
    }

    private void updateAccountProfile(
            Account account,
            ChangeProfileRequest request,
            ProfileImageName changedProfileImageName
    ) {
        account.changeProfile(request.changedNickname(), changedProfileImageName);
    }

    private void updateAccountCareer(Account account, ChangeCareerRequest request) {
        account.changeCareer(
                request.changedJobGroupName(),
                request.changedCompanyName(),
                request.changedExperienceName()
        );
    }

    private AccountResponse convertAccountResponse(Account account) {
        return mapper.toDto(account);
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findBy(accountId)
                                .orElseThrow(() -> new ForbiddenAccountException("존재하지 않는 회원이거나 이미 탈퇴한 회원입니다."));
    }
}
