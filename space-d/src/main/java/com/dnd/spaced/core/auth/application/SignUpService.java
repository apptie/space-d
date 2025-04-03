package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.exception.NicknameMetadataNotFoundException;
import com.dnd.spaced.core.skill.application.event.dto.InitializedAccountEvent;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService {

    private static final Role DEFAULT_ROLE = Role.ROLE_USER;

    private final AccountRepository accountRepository;
    private final NicknameProperties nicknameProperties;
    private final NicknameMetadataRepository nicknameMetadataRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Account signUp(RegistrationId registrationId, String socialIdentifier) {
        String profileImageName = findRandomProfileImage();
        String formattedNickname = formatNickname();
        Account persistedAccount = persistAccount(
                registrationId,
                socialIdentifier,
                formattedNickname,
                profileImageName
        );

        eventPublisher.publishEvent(new InitializedAccountEvent(persistedAccount.getId()));
        return persistedAccount;
    }

    private String findRandomProfileImage() {
        return ProfileImageName.findRandom()
                               .getImageName();
    }

    private String formatNickname() {
        String nickname = nicknameProperties.generate();
        NicknameMetadata metadata = nicknameMetadataRepository.findBy(nickname)
                                                              .orElseThrow(() -> new NicknameMetadataNotFoundException("닉네임 메타데이터가 정상적으로 초기화되지 않았습니다."));

        metadata.addCount();
        return nicknameProperties.format(
                metadata.getNickname(),
                metadata.getTotalCount()
        );
    }

    private Account persistAccount(
            RegistrationId registrationId,
            String socialIdentifier,
            String formattedNickname,
            String profileImageName
    ) {
        Account newAccount = Account.builder()
                                    .registrationId(registrationId)
                                    .socialIdentifier(socialIdentifier)
                                    .nickname(formattedNickname)
                                    .role(DEFAULT_ROLE)
                                    .profileImage(profileImageName)
                                    .build();

        return accountRepository.save(newAccount);
    }
}
