package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
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

    private static final Role DEFAULT_ROLE = Role.ROLE_USER;

    private final NicknameProperties nicknameProperties;
    private final AccountRepository accountRepository;
    private final NicknameMetadataRepository nicknameMetadataRepository;

    @Transactional
    public LoggedInAccountInfoDto login(String registrationIdName, String socialIdentifier) {
        AtomicBoolean isSignUp = new AtomicBoolean();
        RegistrationId registrationId = RegistrationId.findBy(registrationIdName);
        Account account = findAuthorizedAccount(socialIdentifier, registrationId, isSignUp);

        return new LoggedInAccountInfoDto(account.getId(), account.getRole().name(), isSignUp.get());
    }

    private Account findAuthorizedAccount(
            String socialIdentifier,
            RegistrationId registrationId,
            AtomicBoolean isSignUp
    ) {
        return accountRepository.findBy(registrationId, socialIdentifier)
                                .orElseGet(
                                        () -> processSignUpAccount(
                                                registrationId,
                                                socialIdentifier,
                                                isSignUp
                                        )
                                );
    }

    private Account processSignUpAccount(
            RegistrationId registrationId,
            String socialIdentifier,
            AtomicBoolean isSignUp) {
        isSignUp.set(true);

        String nickname = nicknameProperties.generate();
        String profileImageName = ProfileImageName.findRandom()
                                                  .getImageName();

        return nicknameMetadataRepository.findBy(nickname)
                                         .map(
                                                 nicknameMetadata -> processExistsNicknameMetadata(
                                                         registrationId,
                                                         socialIdentifier,
                                                         nicknameMetadata,
                                                         profileImageName
                                                 )
                                         )
                                         .orElseGet(
                                                 () -> processNotExistsNicknameMetadata(
                                                         registrationId,
                                                         socialIdentifier,
                                                         nickname,
                                                         profileImageName
                                                 )
                                         );
    }

    private Account processExistsNicknameMetadata(
            RegistrationId registrationId,
            String socialIdentifier,
            NicknameMetadata nicknameMetadata,
            String profileImageName
    ) {
        nicknameMetadata.addCount();

        return saveAccount(registrationId, socialIdentifier, profileImageName, nicknameMetadata);
    }

    private Account processNotExistsNicknameMetadata(
            RegistrationId registrationId,
            String socialIdentifier,
            String nickname,
            String profileImageName
    ) {
        NicknameMetadata nicknameMetadata = NicknameMetadata.from(nickname);

        nicknameMetadataRepository.save(nicknameMetadata);

        return saveAccount(registrationId, socialIdentifier, profileImageName, nicknameMetadata);
    }

    private Account saveAccount(
            RegistrationId registrationId,
            String socialIdentifier,
            String profileImage,
            NicknameMetadata nicknameMetadata
    ) {
        String nickname = nicknameProperties.format(
                nicknameMetadata.getNickname(),
                nicknameMetadata.getCount()
        );
        Account account = Account.builder()
                                 .registrationId(registrationId)
                                 .socialIdentifier(socialIdentifier)
                                 .nickname(nickname)
                                 .role(DEFAULT_ROLE)
                                 .profileImage(profileImage)
                                 .build();

        return accountRepository.save(account);
    }
}
