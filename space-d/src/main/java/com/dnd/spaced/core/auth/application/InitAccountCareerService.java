package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.auth.application.dto.request.InitAccountCareerRequest;
import com.dnd.spaced.core.auth.application.exception.ForbiddenInitCareerInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InitAccountCareerService {

    private final AccountRepository accountRepository;

    @Transactional
    public void initCareer(Long accountId, InitAccountCareerRequest request) {
        Account account = findPreInitAccount(accountId);

        executeCareerInit(request, account);
    }

    private Account findPreInitAccount(Long accountId) {
        return accountRepository.findPreInitializationAccountBy(accountId)
                                .orElseThrow(
                                        () -> new ForbiddenInitCareerInfoException(
                                                "최초로 가입한 회원이 아닙니다."
                                        )
                                );
    }

    private void executeCareerInit(InitAccountCareerRequest request, Account account) {
        account.changeCareer(request.jobGroupName(), request.companyName(), request.experienceName());
    }
}
