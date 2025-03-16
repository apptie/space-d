package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;
import static com.dnd.spaced.core.word.domain.QWord.word;
import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import com.dnd.spaced.core.word.infrastructure.persistence.util.WordSortConditionConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        return wordCrudRepository.existsById(wordId);
    }

    @Override
    public void updateViewCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.viewCount, word.viewCount.add(1))
                    .where(word.id.eq(wordId))
                    .execute();
    }

    @Override
    public void updateViewCount(List<WordViewCountStatisticsDto> wordViewCountStatisticsDtos) {
        for (WordViewCountStatisticsDto dto : wordViewCountStatisticsDtos) {
            queryFactory.update(word)
                        .set(word.viewCount, word.viewCount.add(dto.viewCount()))
                        .where(word.id.eq(dto.id()))
                        .execute();
        }
    }

    @Override
    public void addBookmarkCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.bookmarkCount, word.bookmarkCount.add(1))
                    .where(word.id.eq(wordId))
                    .execute();
    }

    @Override
    public void updateSubtractBookmarkCount(Long wordId) {
        queryFactory.update(word)
                    .set(word.bookmarkCount, word.bookmarkCount.subtract(1))
                    .where(word.id.eq(wordId))
                    .execute();
    }

    @Override
    public Optional<Word> findBy(Long wordId) {
        Word result = queryFactory.selectFrom(word)
                                  .leftJoin(word.wordExamples)
                                  .leftJoin(word.pronunciations).fetchJoin()
                                  .where(word.id.eq(wordId))
                                  .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<String> findNameAllBy(Long[] wordIds) {
        return queryFactory.select(word.name)
                           .from(word)
                           .where(word.id.in(wordIds))
                           .fetch();
    }

    @Override
    public List<Word> findAllBy(Category category, String lastWordName, Pageable pageable) {
        return queryFactory.selectFrom(word)
                           .where(gtLastWordName(lastWordName), eqCategory(category))
                           .orderBy(
                                   WordSortConditionConverter.convert(pageable)
                                                             .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageable.getPageSize())
                           .fetch();
    }

    @Override
    public List<Word> search(WordSearchCondition condition, WordSearchPageRequest pageRequest) {
        return queryFactory.selectFrom(word)
                           .join(word.pronunciations, pronunciation)
                           .on(
                                   calculatePronunciationBooleanExpression(condition.pronunciation())
                                           .toArray(BooleanExpression[]::new)
                           )
                           .where(
                                   gtLastWordName(pageRequest.lastWordName()),
                                   nameStartsWith(condition.name()),
                                   eqCategory(condition.category())
                           )
                           .orderBy(
                                   WordSortConditionConverter.convert(pageRequest.pageable())
                                                             .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageRequest.pageable().getPageSize())
                           .fetch();
    }

    @Override
    public List<Word> findAllBy(List<Long> wordIds) {
        List<Word> words = queryFactory.selectFrom(word)
                                       .leftJoin(word.wordExamples, wordExample)
                                       .where(word.id.in(wordIds.toArray(Long[]::new)))
                                       .fetch();

        Collections.shuffle(words);

        return words;
    }

    private BooleanExpression gtLastWordName(String lastWordName) {
        if (lastWordName == null) {
            return null;
        }

        return word.name.gt(lastWordName);
    }

    private BooleanExpression nameStartsWith(String name) {
        if (name == null) {
            return null;
        }

        return word.name.startsWith(name);
    }

    private BooleanExpression eqCategory(Category category) {
        if (category == null) {
            return null;
        }

        return word.category.eq(category);
    }

    private List<BooleanExpression> calculatePronunciationBooleanExpression(String content) {
        List<BooleanExpression> pronunciationPredicate = new ArrayList<>();

        pronunciationPredicate.add(pronunciation.word.id.eq(word.id));

        if (content != null) {
            pronunciationPredicate.add(pronunciation.content.startsWith(content));
        }

        return pronunciationPredicate;
    }
}
