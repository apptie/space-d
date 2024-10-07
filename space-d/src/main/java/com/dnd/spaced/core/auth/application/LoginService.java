package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.ProfileImageName;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private static final String DEFAULT_ROLE_NAME = Role.ROLE_USER.name();

    private final NicknameProperties nicknameProperties;
    private final AccountRepository accountRepository;
    private final NicknameMetadataRepository nicknameMetadataRepository;

    @Transactional
    public LoggedInAccountInfoDto login(String accountId) {
        AtomicBoolean isSignUp = new AtomicBoolean();
        Account account = accountRepository.findBy(accountId)
                                           .orElseGet(() -> processSignUpAccount(accountId, isSignUp));

        return new LoggedInAccountInfoDto(account.getId(), account.getRole().name(), isSignUp.get());
    }

    private Account processSignUpAccount(String accountId, AtomicBoolean isSignUp) {
        isSignUp.set(true);

        return signUp(accountId);
    }

    private Account signUp(String accountId) {
        String nickname = nicknameProperties.generate();
        String profileImageName = ProfileImageName.findRandom()
                                                  .getImageName();

        return nicknameMetadataRepository.findBy(nickname)
                                         .map(
                                                 nicknameMetadata -> processExistsNicknameMetadata(
                                                         accountId,
                                                         nicknameMetadata,
                                                         profileImageName
                                                 )
                                         )
                                         .orElseGet(
                                                 () -> processNotExistsNicknameMetadata(
                                                         accountId,
                                                         nickname,
                                                         profileImageName
                                                 )
                                         );
    }

    private Account processExistsNicknameMetadata(String accountId, NicknameMetadata nicknameMetadata, String profileImageName) {
        nicknameMetadata.addCount();

        return saveAccount(accountId, profileImageName, nicknameMetadata);
    }

    private Account processNotExistsNicknameMetadata(String accountId, String nickname, String profileImageName) {
        NicknameMetadata nicknameMetadata = new NicknameMetadata(nickname);

        nicknameMetadataRepository.save(nicknameMetadata);

        return saveAccount(accountId, profileImageName, nicknameMetadata);
    }

    private Account saveAccount(String accountId, String profileImage, NicknameMetadata nicknameMetadata) {
        String nickname = nicknameProperties.format(
                nicknameMetadata.getNickname(),
                nicknameMetadata.getCount()
        );
        Account account = Account.builder()
                                 .id(accountId)
                                 .nickname(nickname)
                                 .roleName(DEFAULT_ROLE_NAME)
                                 .profileImage(profileImage)
                                 .build();

        return accountRepository.save(account);
    }
}
