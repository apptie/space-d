package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordMetadataGatewayRepository implements WordMetadataRepository {

    private final WordMetadataCrudRepository wordMetadataCrudRepository;

    @Override
    public void save(WordMetadata wordMetadata) {
        wordMetadataCrudRepository.save(wordMetadata);
    }

    @Override
    public Optional<WordMetadata> findBy(Long id) {
        return wordMetadataCrudRepository.findById(id);
    }
}
