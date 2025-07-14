package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.Social;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final SignUpService signUpService;
    private final AccountRepository accountRepository;

    public LoggedInAccountDto login(String registrationIdName, String socialIdentifier) {
        RegistrationId registrationId = RegistrationId.findBy(registrationIdName);
        Social social = new Social(registrationId, socialIdentifier);

        return accountRepository.findBy(social)
                                .map(this::buildLoggedInAccount)
                                .orElseGet(() -> buildSignUpAccount(registrationId, socialIdentifier));
    }

    private LoggedInAccountDto buildLoggedInAccount(Account account) {
        return new LoggedInAccountDto(account.getId(), account.getRole().name(), false);
    }

    private LoggedInAccountDto buildSignUpAccount(RegistrationId registrationId, String socialIdentifier) {
        Account signedUpAccount = signUpService.signUp(registrationId, socialIdentifier);

        return new LoggedInAccountDto(signedUpAccount.getId(), signedUpAccount.getRole().name(), true);
    }
}
