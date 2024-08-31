package com.dnd.spaced.core.account.domain.repository;

import com.dnd.spaced.core.account.domain.Account;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findBy(String id);
}
