package com.dnd.spaced.core.account.infrastructure.persistence;

import static com.dnd.spaced.core.account.domain.QAccount.account;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.Social;
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
                                     .where(eqAccountId(accountId))
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
                                     .where(eqAccountId(accountId))
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findBy(Social social) {
        Account result = queryFactory.selectFrom(account)
                                     .where(eqSocial(social))
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findPreInitializationAccountBy(Long accountId) {
        Account result = queryFactory.selectFrom(account)
                                     .where(eqAccountId(accountId), isNullCareer())
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression eqSocial(Social social) {
        return account.social.socialIdentifier.eq(social.getSocialIdentifier())
                .and(account.deleted.isFalse())
                .and(account.social.registrationId.eq(social.getRegistrationId()));
    }

    private BooleanExpression isNullCareer() {
        return account.career.company.isNull()
                                         .and(account.career.experience.isNull())
                                         .and(account.career.jobGroup.isNull());
    }

    private BooleanExpression eqAccountId(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return account.id.eq(accountId).and(account.deleted.isFalse());
    }
}
