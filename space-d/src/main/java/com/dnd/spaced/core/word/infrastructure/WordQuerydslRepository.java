package com.dnd.spaced.core.word.infrastructure;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;
import static com.dnd.spaced.core.word.domain.QWord.word;

import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.infrastructure.util.WordSortConditionConverter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordQuerydslRepository implements WordRepository {

    private final JPAQueryFactory queryFactory;
    private final WordCrudRepository wordCrudRepository;

    @Override
    public Word save(Word word) {
        return wordCrudRepository.save(word);
    }

    @Override
    public void updateViewCount(Long id) {
        queryFactory.update(word)
                    .set(word.viewCount, word.viewCount.add(1))
                    .where(word.id.eq(id))
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
    public Optional<Word> findBy(Long id) {
        Word result = queryFactory.selectFrom(word)
                                  .leftJoin(word.wordExamples)
                                  .leftJoin(word.pronunciations).fetchJoin()
                                  .where(word.id.eq(id))
                                  .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<String> findNameAllBy(Long[] ids) {
        return queryFactory.select(word.name)
                           .from(word)
                           .where(word.id.in(ids))
                           .fetch();
    }

    @Override
    public List<Word> findAllBy(WordCondition wordCondition, WordPageRequest pageRequest) {
        return queryFactory.selectFrom(word)
                           .where(
                                   lastWordNameGt(pageRequest.lastWordName()),
                                   categoryEq(wordCondition.category())
                           )
                           .orderBy(
                                   WordSortConditionConverter.convert(pageRequest.pageable())
                                                             .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageRequest.pageable().getPageSize())
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
                                   lastWordNameGt(pageRequest.lastWordName()),
                                   nameStartsWith(condition.name()),
                                   categoryEq(condition.category())
                           )
                           .orderBy(
                                   WordSortConditionConverter.convert(pageRequest.pageable())
                                                             .toArray(OrderSpecifier[]::new)
                           )
                           .limit(pageRequest.pageable().getPageSize())
                           .fetch();
    }

    private BooleanExpression lastWordNameGt(String lastWordName) {
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

    private BooleanExpression categoryEq(Category category) {
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
