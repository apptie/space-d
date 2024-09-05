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

    @Override
    public void update(Long id, String example) {
        queryFactory.update(wordExample)
                    .set(wordExample.example, example)
                    .where(wordExample.id.eq(id))
                    .execute();
    }

    @Override
    public void deleteBy(Long id) {
        wordExampleCrudRepository.deleteById(id);
    }
}
