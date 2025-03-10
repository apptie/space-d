package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordExampleGatewayRepository implements WordExampleRepository {

    private final JPAQueryFactory queryFactory;
    private final WordExampleCrudRepository wordExampleCrudRepository;

    public long countBy(Long wordExampleId) {
        return queryFactory.select(wordExample.id.count())
                           .from(wordExample)
                           .where(wordExample.word.id.eq(wordExampleId))
                           .fetchFirst();
    }

    @Override
    public long update(Long wordExampleId, String example) {
        return queryFactory.update(wordExample)
                           .set(wordExample.example, example)
                           .where(wordExample.id.eq(wordExampleId))
                           .execute();
    }

    @Override
    public void deleteBy(Long wordExampleId) {
        wordExampleCrudRepository.deleteById(wordExampleId);
    }
}
