package com.dnd.spaced.core.admin.application.helper;

import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class WithWordMetadataTestHelper {

    @Autowired
    WordMetadataRepository wordMetadataRepository;

    protected WordMetadata wordMetadata;

    @BeforeEach
    void beforeEach() {
        wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
    }
}
