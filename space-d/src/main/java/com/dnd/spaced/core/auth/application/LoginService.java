package com.dnd.spaced.core.auth.application;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import com.dnd.spaced.global.config.properties.ProfileImageProperties;
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
    private final ProfileImageProperties profileImageProperties;
    private final AccountRepository accountRepository;
    private final NicknameMetadataRepository nicknameMetadataRepository;

    @Transactional
    public LoggedInAccountInfoDto login(String id) {
        AtomicBoolean isSignUp = new AtomicBoolean(false);
        Account account = accountRepository.findBy(id)
                                           .orElseGet(
                                                   () -> {
                                                       isSignUp.set(true);
                                                       return signUp(id);
                                                   }
                                           );

        return new LoggedInAccountInfoDto(account.getId(), account.getRole().name(), isSignUp.get());
    }

    private Account signUp(String id) {
        String nickname = nicknameProperties.generate();
        String profileImage = profileImageProperties.find();

        return nicknameMetadataRepository.findBy(nickname)
                                         .map(
                                                 nicknameMetadata -> {
                                                     nicknameMetadata.addCount();
                                                     return saveAccount(id, profileImage, nicknameMetadata);
                                                 }
                                         )
                                         .orElseGet(
                                                 () -> {
                                                     NicknameMetadata nicknameMetadata = new NicknameMetadata(nickname);

                                                     nicknameMetadataRepository.save(nicknameMetadata);
                                                     return saveAccount(id, profileImage, nicknameMetadata);
                                                 }
                                         );
    }

    private Account saveAccount(String id, String profileImage, NicknameMetadata nicknameMetadata) {
        String nickname = nicknameProperties.format(
                nicknameMetadata.getNickname(),
                nicknameMetadata.getCount()
        );
        Account account = Account.builder()
                                 .id(id)
                                 .nickname(nickname)
                                 .roleName(DEFAULT_ROLE_NAME)
                                 .profileImage(profileImage)
                                 .build();

        return accountRepository.save(account);
    }
}
