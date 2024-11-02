package com.dnd.spaced.core.account.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.Role;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountCrudRepositoryTest {

    @Autowired
    AccountCrudRepository accountCrudRepository;

    @Autowired
    EntityManager em;

    @Test
    void 회원을_soft_delete로_삭제한다() {
        // given
        Account account = Account.builder()
                                 .id("id1")
                                 .nickname("nickname")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();

        accountCrudRepository.save(account);

        // when
        accountCrudRepository.delete(account);

        // then
        em.flush();

        Optional<Account> actual = accountCrudRepository.findById(account.getId());

        assertThat(actual).isEmpty();
    }

    @Test
    void soft_delete_되지_않은_회원을_조회한다() {
        // given
        Account account1 = Account.builder()
                                 .id("id1")
                                 .nickname("nickname1")
                                 .profileImage("profileImage")
                                 .roleName(Role.ROLE_ADMIN.name())
                                 .build();
        Account account2 = Account.builder()
                                  .id("id2")
                                  .nickname("nickname2")
                                  .profileImage("profileImage")
                                  .roleName(Role.ROLE_ADMIN.name())
                                  .build();

        accountCrudRepository.saveAll(List.of(account1, account2));
        accountCrudRepository.delete(account2);

        em.flush();

        // when
        Optional<Account> actualAccount1 = accountCrudRepository.findById(account1.getId());
        Optional<Account> actualAccount2 = accountCrudRepository.findById(account2.getId());

        // then
        assertAll(
                () -> assertThat(actualAccount1).isPresent(),
                () -> assertThat(actualAccount2).isEmpty()
        );
    }
}
