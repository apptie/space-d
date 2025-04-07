package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QWordMetadata.*;

import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordMetadataGatewayRepository implements WordMetadataRepository {

    private final JPAQueryFactory queryFactory;
    private final WordMetadataCrudRepository wordMetadataCrudRepository;

    @Override
    public void save(WordMetadata wordMetadata) {
        wordMetadataCrudRepository.save(wordMetadata);
    }

    @Override
    public Optional<WordMetadata> findBy(Long wordMetadataId) {
        return wordMetadataCrudRepository.findById(wordMetadataId);
    }

    @Override
    public void update(Category category) {
        JPAUpdateClause wordMetadataUpdateClause = queryFactory.update(wordMetadata)
                                                               .set(wordMetadata.totalWordCount, wordMetadata.totalWordCount.add(1));

        calculateWordMetadataUpdateClause(category, wordMetadataUpdateClause).execute();
    }

    private JPAUpdateClause calculateWordMetadataUpdateClause(
            Category category,
            JPAUpdateClause wordMetadataUpdateClause
    ) {
        wordMetadataUpdateClause.set(wordMetadata.totalWordCount, wordMetadata.totalWordCount.add(1));

        if (category.isBusiness()) {
            return wordMetadataUpdateClause.set(wordMetadata.businessWordCount, wordMetadata.businessWordCount.add(1));
        }
        if (category.isDesign()) {
            return wordMetadataUpdateClause.set(wordMetadata.designWordCount, wordMetadata.designWordCount.add(1));
        }

        return wordMetadataUpdateClause.set(wordMetadata.developWordCount, wordMetadata.developWordCount.add(1));
    }
}
