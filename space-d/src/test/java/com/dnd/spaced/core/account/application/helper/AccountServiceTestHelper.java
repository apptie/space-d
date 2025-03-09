package com.dnd.spaced.core.account.application.helper;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AccountServiceTestHelper {

    @Autowired
    protected AccountRepository accountRepository;

    protected Account account;

    @BeforeEach
    void beforeEach() {
        account = Account.builder()
                         .registrationId(RegistrationId.KAKAO)
                         .socialIdentifier("12345")
                         .nickname("재빠른지구001")
                         .profileImage("earth.png")
                         .role(Role.ROLE_USER)
                         .build();

        account.changeCareerInfo(
                "디자이너",
                "대기업",
                "3~4년 차"
        );

        accountRepository.save(account);
    }
}
