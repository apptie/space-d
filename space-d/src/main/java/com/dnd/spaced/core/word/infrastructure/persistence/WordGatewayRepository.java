package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QWord.word;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordGatewayRepository implements WordRepository {

    private final JPAQueryFactory queryFactory;
    private final WordCrudRepository wordCrudRepository;

    @Override
    public Word save(Word word) {
        return wordCrudRepository.save(word);
    }

    @Override
    public boolean existsBy(Long wordId) {
        Long result = queryFactory.select(word.id)
                                  .from(word)
                                  .where(word.id.eq(wordId), word.deleted.isFalse())
                                  .fetchOne();

        return result != null;
    }

    @Override
    public void updateViewCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.viewCount, word.viewCount.add(1))
                    .where(word.id.eq(wordId), word.deleted.isFalse())
                    .execute();
    }

    @Override
    public void updateViewCount(List<WordViewCountStatisticsDto> wordViewCountStatisticsDtos) {
        for (WordViewCountStatisticsDto dto : wordViewCountStatisticsDtos) {
            queryFactory.update(word)
                        .set(word.viewCount, word.viewCount.add(dto.viewCount()))
                        .where(word.id.eq(dto.id()), word.deleted.isFalse())
                        .execute();
        }
    }

    @Override
    public void addBookmarkCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.bookmarkCount, word.bookmarkCount.add(1))
                    .where(word.id.eq(wordId), word.deleted.isFalse())
                    .execute();
    }

    @Override
    public void updateSubtractBookmarkCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.bookmarkCount, word.bookmarkCount.subtract(1))
                    .where(word.id.eq(wordId), word.deleted.isFalse())
                    .execute();
    }

    @Override
    public List<String> findNameAllBy(Long[] wordIds) {
        return queryFactory.select(word.name)
                           .from(word)
                           .where(word.id.in(wordIds), word.deleted.isFalse())
                           .fetch();
    }

    @Override
    public Optional<Word> findBy(Long wordId) {
        Word result = queryFactory.selectFrom(word)
                                  .where(word.id.eq(wordId), word.deleted.isFalse())
                                  .fetchOne();

        return Optional.ofNullable(result);
    }
}
