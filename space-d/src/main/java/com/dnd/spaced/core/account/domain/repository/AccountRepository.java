package com.dnd.spaced.core.account.domain.repository;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import java.util.Optional;

public interface AccountRepository {

    boolean existsBy(Long accountId);

    Account save(Account account);

    Optional<Account> findBy(Long accountId);

    Optional<Account> findBy(RegistrationId registrationId, String socialIdentifier);

    Optional<Account> findPreInitializationAccountBy(Long accountId);
}
