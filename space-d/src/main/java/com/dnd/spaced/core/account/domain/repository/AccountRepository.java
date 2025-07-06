package com.dnd.spaced.core.account.domain.repository;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.SocialInfo;
import java.util.Optional;

public interface AccountRepository {

    boolean existsBy(Long accountId);

    Account save(Account account);

    Optional<Account> findBy(Long accountId);

    Optional<Account> findBy(SocialInfo socialInfo);

    Optional<Account> findPreInitializationAccountBy(Long accountId);
}
