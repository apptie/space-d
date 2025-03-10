package com.dnd.spaced.core.like.application.helper;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.core.comment.domain.Comment;
import com.dnd.spaced.core.comment.domain.repository.CommentRepository;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class WithAccountAndCommentTestHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    CommentRepository commentRepository;

    protected Account account;
    protected Comment comment;


    @BeforeEach
    void beforeEach() {
        this.account = Account.builder()
                              .registrationId(RegistrationId.KAKAO)
                              .socialIdentifier("12345")
                              .nickname("재빠른지구001")
                              .profileImage("earth.png")
                              .role(Role.ROLE_USER)
                              .build();
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();
        this.comment = new Comment(account.getId(), word.getId(), "이 용어는 언제 쓰는건가요?");

        accountRepository.save(account);
        wordRepository.save(word);
        commentRepository.save(comment);
    }
}
