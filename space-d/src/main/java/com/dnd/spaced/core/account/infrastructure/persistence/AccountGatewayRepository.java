package com.dnd.spaced.core.account.infrastructure.persistence;

import static com.dnd.spaced.core.account.domain.QAccount.account;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountGatewayRepository implements AccountRepository {

    private final JPAQueryFactory queryFactory;
    private final AccountCrudRepository accountCrudRepository;

    /**
     * public boolean existsByName(String name) {
     *     Integer result = queryFactory
     *         .selectOne() // 1. 1값 프로젝션
     *         .from(member)
     *         .where(member.name.eq(name))
     *         .fetchFirst(); // 2. LIMIT 1 적용
     *
     *     return result != null;
     * }
     * @param account
     * @return
     */

    @Override
    public Account save(Account account) {
        return accountCrudRepository.save(account);
    }

    @Override
    public Optional<Account> findBy(Long accountId) {
        Account result = queryFactory.selectFrom(account)
                                       .where(eqAccountId(accountId), account.deleted.isFalse())
                                       .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findBy(RegistrationId registrationId, String socialIdentifier) {
        Account result = queryFactory.selectFrom(account)
                                     .where(
                                             account.socialInfo.registrationId.eq(registrationId),
                                             account.socialInfo.socialIdentifier.eq(socialIdentifier),
                                             account.deleted.isFalse()
                                     )
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findPreInitializationAccountBy(Long accountId) {
        Account result = queryFactory.selectFrom(account)
                                     .where(
                                             account.id.eq(accountId),
                                             account.careerInfo.company.isNull(),
                                             account.careerInfo.experience.isNull(),
                                             account.careerInfo.jobGroup.isNull(),
                                             account.deleted.isFalse()
                                     )
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression eqAccountId(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return account.id.eq(accountId);
    }
}
