package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.enums.Category;
import java.util.Optional;

public interface WordMetadataRepository {

    void save(WordMetadata wordMetadata);

    Optional<WordMetadata> findBy(Long wordMetadataId);

    void update(Category category);
}
