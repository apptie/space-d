package com.dnd.spaced.core.quiz.application.helper;

import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class WithWordMetadataTestHelper {

    @Autowired
    WordMetadataRepository wordMetadataRepository;

    @BeforeEach
    void beforeEach() {
        wordMetadataRepository.save(new WordMetadata());
    }
}
