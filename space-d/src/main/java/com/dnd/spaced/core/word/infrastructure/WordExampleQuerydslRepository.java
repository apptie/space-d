package com.dnd.spaced.core.word.infrastructure;

import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordExampleQuerydslRepository implements WordExampleRepository {

    private final JPAQueryFactory queryFactory;
    private final WordExampleCrudRepository wordExampleCrudRepository;

    public long countBy(Long wordId) {
        return queryFactory.select(wordExample.id.count())
                           .from(wordExample)
                           .where(wordExample.word.id.eq(wordId))
                           .fetchFirst();
    }

    @Override
    public long update(Long id, String example) {
        return queryFactory.update(wordExample)
                           .set(wordExample.example, example)
                           .where(wordExample.id.eq(id))
                           .execute();
    }

    @Override
    public void deleteBy(Long id) {
        wordExampleCrudRepository.deleteById(id);
    }
}
