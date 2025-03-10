package com.dnd.spaced.core.comment.application.helper;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class WithWriterAndReaderAndWordTestHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WordRepository wordRepository;

    protected Account writer;

    protected Account reader;

    protected Word word;

    @BeforeEach
    void beforeEach() {
        writer = Account.builder()
                        .registrationId(RegistrationId.KAKAO)
                        .socialIdentifier("12345")
                        .nickname("재빠른지구001")
                        .profileImage("earth.png")
                        .role(Role.ROLE_USER)
                        .build();
        reader = Account.builder()
                        .registrationId(RegistrationId.KAKAO)
                        .socialIdentifier("54321")
                        .nickname("재빠른지구002")
                        .profileImage("earth.png")
                        .role(Role.ROLE_USER)
                        .build();
        word = Word.builder()
                   .name("Authorization")
                   .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                   .categoryName("개발")
                   .build();

        accountRepository.save(writer);
        accountRepository.save(reader);
        wordRepository.save(word);
    }
}
