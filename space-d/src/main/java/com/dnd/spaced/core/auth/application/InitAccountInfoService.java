package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.auth.application.exception.ForbiddenInitCareerInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InitAccountInfoService {

    private final AccountRepository accountRepository;

    @Transactional
    public void initCareerInfo(String id, String jobGroupName, String companyName, String experienceName) {
        Account account = accountRepository.findBy(id)
                                           .orElseThrow(
                                                   () -> new ForbiddenInitCareerInfoException(
                                                           "로그인이 필요한 기능입니다."
                                                   )
                                           );

        account.changeCareerInfo(jobGroupName, companyName, experienceName);
    }
}
