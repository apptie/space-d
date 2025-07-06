package com.dnd.spaced.core.account.infrastructure.persistence;

import static com.dnd.spaced.core.account.domain.QAccount.account;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.SocialInfo;
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
    public Optional<Account> findBy(SocialInfo socialInfo) {
        Account result = queryFactory.selectFrom(account)
                                     .where(eqSocialInfo(socialInfo))
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Account> findPreInitializationAccountBy(Long accountId) {
        Account result = queryFactory.selectFrom(account)
                                     .where(eqAccountId(accountId), isNullCareerInfo())
                                     .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression eqSocialInfo(SocialInfo socialInfo) {
        return account.socialInfo.socialIdentifier.eq(socialInfo.getSocialIdentifier())
                .and(account.deleted.isFalse())
                .and(account.socialInfo.registrationId.eq(socialInfo.getRegistrationId()));
    }

    private BooleanExpression isNullCareerInfo() {
        return account.careerInfo.company.isNull()
                                         .and(account.careerInfo.experience.isNull())
                                         .and(account.careerInfo.jobGroup.isNull());
    }

    private BooleanExpression eqAccountId(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return account.id.eq(accountId).and(account.deleted.isFalse());
    }
}
