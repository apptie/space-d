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

    @Override
    public boolean existsBy(Long accountId) {
        Integer result = queryFactory.selectOne()
                                .from(account)
                                .where(account.id.eq(accountId))
                                .fetchFirst();

        return result != null;
    }

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
                                             account.socialInfo.socialIdentifier.eq(socialIdentifier),
                                             account.deleted.isFalse(),
                                             account.socialInfo.registrationId.eq(registrationId)
                                             )
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findPreInitializationAccountBy(Long accountId) {
        Account result = queryFactory.selectFrom(account)
                                     .where(
                                             account.id.eq(accountId),
                                             account.deleted.isFalse(),
                                             isNullCareerInfo()
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

    private BooleanExpression isNullCareerInfo() {
        return account.careerInfo.company.isNull()
                                         .or(account.careerInfo.experience.isNull())
                                         .or(account.careerInfo.jobGroup.isNull());
    }
}
