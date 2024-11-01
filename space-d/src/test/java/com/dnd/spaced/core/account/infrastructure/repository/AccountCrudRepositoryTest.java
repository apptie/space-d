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
    void delete_메서드는_soft_delete를_수행한다() {
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

        em.flush();

        // then
        Optional<Account> actual = accountCrudRepository.findById(account.getId());

        assertThat(actual).isEmpty();
    }

    @Test
    void findById_메서드는_soft_delete_대상을_제외한_데이터를_조회한다() {
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
        assertAll(
                () -> assertThat(accountCrudRepository.findById(account1.getId())).isPresent(),
                () -> assertThat(accountCrudRepository.findById(account2.getId())).isEmpty()
        );
    }
}
