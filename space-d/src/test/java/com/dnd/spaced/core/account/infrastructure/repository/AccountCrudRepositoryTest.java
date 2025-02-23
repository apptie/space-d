package com.dnd.spaced.core.account.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
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
    void 회원을_식별자로_삭제한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
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
    void 삭제되지_않은_회원을_식별자로_조회한다() {
        // given
        Account account1 = Account.builder()
                                  .registrationId(RegistrationId.KAKAO)
                                  .socialIdentifier("12345")
                                  .nickname("재빠른지구001")
                                  .profileImage("earth.png")
                                  .role(Role.ROLE_USER)
                                  .build();
        Account account2 = Account.builder()
                                  .registrationId(RegistrationId.KAKAO)
                                  .socialIdentifier("54321")
                                  .nickname("재빠른지구002")
                                  .profileImage("earth.png")
                                  .role(Role.ROLE_USER)
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
