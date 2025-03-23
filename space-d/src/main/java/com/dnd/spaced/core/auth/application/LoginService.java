package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.core.auth.application.exception.NicknameMetadataNotFoundException;
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
                                         .orElseThrow(() -> new NicknameMetadataNotFoundException("닉네임 메타데이터가 정상적으로 초기화되지 않았습니다."));
    }

    private Account processExistsNicknameMetadata(
            RegistrationId registrationId,
            String socialIdentifier,
            NicknameMetadata nicknameMetadata,
            String profileImageName
    ) {
        nicknameMetadata.addCount();

        return persistAccount(registrationId, socialIdentifier, profileImageName, nicknameMetadata);
    }

    private Account persistAccount(
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
