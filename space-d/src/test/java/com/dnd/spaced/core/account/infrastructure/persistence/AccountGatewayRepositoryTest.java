package com.dnd.spaced.core.account.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    private static final long ACCOUNT_ID = 1L;
    private static final long DELETED_ACCOUNT_ID = 2L;
    private static final long PRE_INIT_ACCOUNT_ID = 3L;
    private static final long DELETED_PRE_INIT_ACCOUNT_ID = 4L;
    private static final RegistrationId REGISTRATION_ID = RegistrationId.KAKAO;
    private static final String SOCIAL_ID = "12345";
    private static final String DELETED_SOCIAL_ID = "54321";

    @Autowired
    AccountRepository accountRepository;

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴하지_않은_회원의_id로_영속화_여부를_확인한다() {
        // when
        boolean actual = accountRepository.existsBy(ACCOUNT_ID);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴한_회원의_id로_영속화_여부를_확인한다() {
        // when
        boolean actual = accountRepository.existsBy(DELETED_ACCOUNT_ID);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴하지_않은_회원을_id로_조회한다() {
        // when
        Optional<Account> actual = accountRepository.findBy(ACCOUNT_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(ACCOUNT_ID)
        );
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴한_회원은_id로_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findBy(DELETED_ACCOUNT_ID);

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
    void 회원의_소셜_id로_탈퇴하지_않은_회원을_조회한다() {
        // given
        Social social = new Social(REGISTRATION_ID, SOCIAL_ID);

        // when
        Optional<Account> actual = accountRepository.findBy(social);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getSocial().getRegistrationId()).isEqualTo(REGISTRATION_ID),
                () -> assertThat(actual.get().getSocial().getSocialId()).isEqualTo(SOCIAL_ID)
        );
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 탈퇴한_회원은_소셜_id로_조회할_수_없다() {
        // given
        Social social = new Social(REGISTRATION_ID, DELETED_SOCIAL_ID);

        // when
        Optional<Account> actual = accountRepository.findBy(social);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 초기_설정만_진행한_회원을_id로_조회한다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(PRE_INIT_ACCOUNT_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(PRE_INIT_ACCOUNT_ID)
        );
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 설정을_모두_완료한_회원을_id로_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(ACCOUNT_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/account/account.sql")
    void 초기_설정_진행_도중_탈퇴한_회원을_id로_조회할_수_없다() {
        // when
        Optional<Account> actual = accountRepository.findPreInitializationAccountBy(DELETED_PRE_INIT_ACCOUNT_ID);

        // then
        assertThat(actual).isEmpty();
    }
}
