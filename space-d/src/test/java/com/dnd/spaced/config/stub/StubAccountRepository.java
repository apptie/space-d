package com.dnd.spaced.config.stub;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import java.util.Optional;

public class StubAccountRepository implements AccountRepository {

    @Override
    public boolean existsBy(Long accountId) {
        return true;
    }

    @Override
    public Account save(Account account) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public Optional<Account> findBy(Long accountId) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public Optional<Account> findBy(RegistrationId registrationId, String socialIdentifier) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public Optional<Account> findPreInitializationAccountBy(Long accountId) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }
}
