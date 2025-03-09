package com.dnd.spaced.core.bookmark.application.helper;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class WithWordTestHelper {

    @Autowired
    WordRepository wordRepository;

    protected Word word;

    @BeforeEach
    void beforeEach() {
        String name = "Authorization";
        String categoryName = "개발";
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        word = Word.builder()
                   .name(name)
                   .categoryName(categoryName)
                   .meaning(meaning)
                   .build();

        wordRepository.save(word);
    }
}
