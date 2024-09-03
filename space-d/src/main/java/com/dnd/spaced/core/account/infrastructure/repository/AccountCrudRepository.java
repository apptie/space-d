package com.dnd.spaced.core.account.infrastructure.repository;

import com.dnd.spaced.core.account.domain.Account;
import org.springframework.data.repository.CrudRepository;

interface AccountCrudRepository extends CrudRepository<Account, String> {
}
