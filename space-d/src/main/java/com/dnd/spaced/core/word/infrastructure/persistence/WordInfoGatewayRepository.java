package com.dnd.spaced.core.word.infrastructure.persistence;

import static com.dnd.spaced.core.word.domain.QPronunciation.pronunciation;
import static com.dnd.spaced.core.word.domain.QWord.word;
import static com.dnd.spaced.core.word.domain.QWordExample.wordExample;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.dto.WordInfo;
import com.dnd.spaced.core.word.domain.dto.mapper.WordInfoMapper;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordInfoRepository;
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
public class WordInfoGatewayRepository implements WordInfoRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<WordInfo> findBy(Long wordId) {
        Word result = findWord(wordId);

        if (result == null) {
            return Optional.empty();
        }

        List<Pronunciation> pronunciations = findPronunciations(result);

        return Optional.of(WordInfoMapper.toDto(result, pronunciations));
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

        return mapToWordInfos(wordIds);
    }

    @Override
    public List<WordInfo> search(WordSearchCondition condition, WordSearchPageRequest pageRequest) {
        List<Long> wordIds = fetchFilteredWordIds(condition, pageRequest);

        if (wordIds.isEmpty()) {
            return Collections.emptyList();
        }

        return mapToWordInfos(wordIds);
    }

    private Word findWord(Long wordId) {
        return queryFactory.selectFrom(word)
                           .leftJoin(word.wordExamples).fetchJoin()
                           .where(word.id.eq(wordId), word.deleted.isFalse())
                           .fetchOne();
    }

    private List<Pronunciation> findPronunciations(Word result) {
        return queryFactory.selectFrom(pronunciation)
                           .where(pronunciation.word.id.eq(result.getId()), pronunciation.deleted.isFalse())
                           .fetch();
    }

    private BooleanExpression buildWordPaginationCondition(
            Category category,
            String lastWordName,
            Category lastCategory
    ) {
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

    private Map<Long, Word> fetchWordsWithExamples(List<Long> wordIds) {
        return queryFactory.selectFrom(word)
                           .innerJoin(word.wordExamples, wordExample).fetchJoin()
                           .where(word.id.in(wordIds), wordExample.deleted.isFalse())
                           .fetch()
                           .stream()
                           .collect(Collectors.toMap(Word::getId, Function.identity()));
    }

    private Map<Long, List<Pronunciation>> fetchPronunciations(List<Long> wordIds) {
        return queryFactory
                .selectFrom(pronunciation)
                .where(pronunciation.word.id.in(wordIds), pronunciation.deleted.isFalse())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        pronunciation -> pronunciation.getWord().getId(),
                        Collectors.mapping(Function.identity(), Collectors.toList())
                ));
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

    private BooleanExpression startsWithWordName(String name) {
        if (name == null) {
            return null;
        }

        return word.name.startsWith(name);
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
                                     pronunciation.content.startsWith(content),
                                     pronunciation.deleted.isFalse()
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
}
