package com.dnd.spaced.core.account.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.Social;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountGatewayRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴하지_않은_회원의_식별자로_영속화_여부를_확인한다() {
        // when
        boolean actual = accountRepository.existsBy(1L);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 탈퇴한_회원의_식별자로_영속화_여부를_확인한다() {
        // when
        boolean actual = accountRepository.existsBy(2L);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴하지_않은_회원을_조회한다() {
        // when
        Optional<Account> actual = accountRepository.findBy(1L);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    @Sql("classpath:sql/account/deleted_account.sql")
    void 탈퇴한_회원은_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findBy(2L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 회원을_영속화_한다() {
        // given
        Account account = Account.builder()
                                 .registrationId(RegistrationId.KAKAO)
                                 .socialIdentifier("12345")
                                 .nickname("재빠른지구001")
                                 .profileImage("earth.png")
                                 .role(Role.ROLE_USER)
                                 .build();

        // when
        Account actual = accountRepository.save(account);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 회원의_소셜_정보로_탈퇴하지_않은_회원을_조회한다() {
        // given
        RegistrationId registrationId = RegistrationId.KAKAO;
        Social social = new Social(registrationId, "12345");

        // when
        Optional<Account> actual = accountRepository.findBy(social);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    @Sql("classpath:sql/account/deleted_account.sql")
    void 회원의_소셜_정보로_탈퇴한_회원을_조회한다() {
        // given
        RegistrationId registrationId = RegistrationId.KAKAO;
        Social social = new Social(registrationId, "54321");

        // when
        Optional<Account> actual = accountRepository.findBy(social);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/account/pre_init_account.sql")
    void 초기_설정된_회원을_식별자로_조회한다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(3L);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 설정을_모두_완료한_회원을_식별자로_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/account/pre_init_account.sql")
    void 탈퇴한_회원을_식별자로_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(4L);

        // then
        assertThat(actual).isEmpty();
    }
}
