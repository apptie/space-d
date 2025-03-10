package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.auth.application.dto.request.InitAccountCareerInfoRequest;
import com.dnd.spaced.core.auth.application.exception.ForbiddenInitCareerInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InitAccountCareerInfoService {

    private final AccountRepository accountRepository;

    @Transactional
    public void initCareerInfo(Long accountId, InitAccountCareerInfoRequest request) {
        Account account = findSignedUpAccount(accountId);

        account.changeCareerInfo(request.jobGroupName(), request.companyName(), request.experienceName());
    }

    private Account findSignedUpAccount(Long accountId) {
        return accountRepository.findSignedUpAccountBy(accountId)
                                .orElseThrow(
                                        () -> new ForbiddenInitCareerInfoException(
                                                "최초로 가입한 회원이 아닙니다."
                                        )
                                );
    }
}
