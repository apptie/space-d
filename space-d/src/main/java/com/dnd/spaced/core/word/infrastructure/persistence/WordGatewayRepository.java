package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;
import static com.dnd.spaced.core.word.domain.QWord.word;
import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.dto.WordInfo;
import com.dnd.spaced.core.word.domain.dto.WordInfoMapper;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    public Optional<WordInfo> findBy(Long wordId) {
        Word result = queryFactory.selectFrom(word)
                                  .leftJoin(word.wordExamples).fetchJoin()
                                  .where(word.id.eq(wordId))
                                  .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        List<Pronunciation> pronunciations = queryFactory.selectFrom(pronunciation)
                                                         .where(pronunciation.word.id.eq(result.getId()))
                                                         .fetch();

        return Optional.of(WordInfoMapper.toDto(result, pronunciations));
    }

    @Override
    public List<String> findNameAllBy(Long[] wordIds) {
        return queryFactory.select(word.name)
                           .from(word)
                           .where(word.id.in(wordIds))
                           .fetch();
    }

    @Override
    public List<WordInfo> findAllBy(Category category, String lastWordName, Category lastCategory, Pageable pageable) {
        List<Long> wordIds = queryFactory.select(word.id)
                                         .from(word)
                                         .where(buildWordPaginationCondition(category, lastWordName, lastCategory))
                                         .orderBy(word.name.asc(), word.category.asc(), word.id.desc())
                                         .limit(pageable.getPageSize())
                                         .fetch();

        if (wordIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Word> wordMap = fetchWordsWithExamples(wordIds);
        Map<Long, List<Pronunciation>> pronunciationMap = fetchPronunciations(wordIds);

        return wordIds.stream()
                      .map(wordId -> WordInfoMapper.toDto(wordMap.get(wordId), pronunciationMap.get(wordId)))
                      .toList();
    }

    private BooleanExpression buildWordPaginationCondition(
            Category category,
            String lastWordName,
            Category lastCategory
    ) {
        if (category == null && lastWordName == null && lastCategory == null) {
            return null;
        }
        if (lastWordName == null && category != null && lastCategory == null) {
            return word.category.eq(category);
        }
        if (lastWordName != null && category != null) {
            return word.name.gt(lastWordName).and(word.category.eq(category));
        }
        if (lastWordName != null && lastCategory != null) {
            return word.name.gt(lastWordName).or(word.name.eq(lastWordName).and(word.category.gt(lastCategory)));
        }

        return null;
    }

    @Override
    public List<WordInfo> search(WordSearchCondition condition, WordSearchPageRequest pageRequest) {
        List<Long> wordIds = fetchFilteredWordIds(condition, pageRequest);

        if (wordIds.isEmpty()) {
            return Collections.emptyList();
        }

        return mapToWordInfos(wordIds);
    }

    @Override
    public List<Word> findRandomAllBy(List<Long> wordIds) {
        List<Word> words = queryFactory.selectFrom(word)
                                       .leftJoin(word.wordExamples, wordExample)
                                       .where(word.id.in(wordIds.toArray(Long[]::new)))
                                       .fetch();

        Collections.shuffle(words);

        return words;
    }

    private List<Long> fetchFilteredWordIds(WordSearchCondition condition, WordSearchPageRequest pageRequest) {
        return queryFactory
                .select(word.id)
                .from(word)
                .where(
                        buildWordPaginationCondition(
                                condition.category(),
                                pageRequest.lastWordName(),
                                pageRequest.lastCategory()
                        ),
                        startsWithWordName(condition.name()),
                        buildPronunciationContentCondition(condition)
                )
                .orderBy(word.name.asc(), word.category.asc(), word.id.desc())
                .limit(pageRequest.pageable().getPageSize())
                .fetch();
    }

    private BooleanExpression buildPronunciationContentCondition(WordSearchCondition condition) {
        if (condition.pronunciationContent() == null) {
            return null;
        }

        return existsPronunciationContentCondition(condition.pronunciationContent());
    }

    private BooleanExpression existsPronunciationContentCondition(String content) {
        return JPAExpressions.selectOne()
                             .from(pronunciation)
                             .where(
                                     pronunciation.word.id.eq(word.id),
                                     pronunciation.content.startsWith(content)
                             )
                             .exists();
    }

    private List<WordInfo> mapToWordInfos(List<Long> wordIds) {
        Map<Long, Word> wordMap = fetchWordsWithExamples(wordIds);
        Map<Long, List<Pronunciation>> pronunciationMap = fetchPronunciations(wordIds);

        return wordIds.stream()
                      .map(id -> WordInfoMapper.toDto(wordMap.get(id), pronunciationMap.get(id)))
                      .toList();
    }

    private Map<Long, Word> fetchWordsWithExamples(List<Long> wordIds) {
        return queryFactory.selectFrom(word)
                           .leftJoin(word.wordExamples).fetchJoin()
                           .where(word.id.in(wordIds))
                           .fetch()
                           .stream()
                           .collect(Collectors.toMap(Word::getId, Function.identity()));
    }

    private Map<Long, List<Pronunciation>> fetchPronunciations(List<Long> wordIds) {
        return queryFactory
                .selectFrom(pronunciation)
                .where(pronunciation.word.id.in(wordIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        pronunciation -> pronunciation.getWord().getId(),
                        Collectors.mapping(Function.identity(), Collectors.toList())
                ));
    }

    private BooleanExpression startsWithWordName(String name) {
        if (name == null) {
            return null;
        }

        return word.name.startsWith(name);
    }
}
