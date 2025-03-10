package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;

import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PronunciationGatewayRepository implements PronunciationRepository {

    private final JPAQueryFactory queryFactory;
    private final PronunciationCrudRepository pronunciationCrudRepository;

    @Override
    public long countBy(Long wordId) {
        return queryFactory.select(pronunciation.id.count())
                           .from(pronunciation)
                           .where(pronunciation.word.id.eq(wordId))
                           .fetchFirst();
    }

    @Override
    public void deleteBy(Long id) {
        pronunciationCrudRepository.deleteById(id);
    }
}
