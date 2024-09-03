package com.dnd.spaced.core.account.infrastructure.repository;

import static com.dnd.spaced.core.account.domain.QAccount.account;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountQuerydslRepository implements AccountRepository {

    private final JPAQueryFactory queryFactory;
    private final AccountCrudRepository accountCrudRepository;

    @Override
    public Account save(Account account) {
        return accountCrudRepository.save(account);
    }

    @Override
    public Optional<Account> findBy(String id) {
        return accountCrudRepository.findById(id);
    }

    @Override
    public Optional<Account> findSignedUpAccountBy(String id) {
        Account result = queryFactory.selectFrom(account)
                                     .where(
                                             notWithdrawal(),
                                             account.careerInfo.company.isNull(),
                                             account.careerInfo.experience.isNull(),
                                             account.careerInfo.jobGroup.isNull()
                                     )
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression notWithdrawal() {
        return account.deleted.isFalse();
    }
}
