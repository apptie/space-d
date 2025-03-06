package com.dnd.spaced.core.word.infrastructure;

import static com.dnd.spaced.core.word.domain.QWordRandom.wordRandom;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.WordRandom;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordRandomGatewayRepository implements WordRandomRepository {

    private static final int RANDOM_BOUND = 1_000_000;
    private static final String IGNORE_CATEGORY_NAME = "전체 실무";

    private final WordRandomCrudRepository wordRandomCrudRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveWith(Long wordId, Category category) {
        int random = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);
        WordRandom wordRandom = new WordRandom(wordId, category, random);

        wordRandomCrudRepository.save(wordRandom);
    }

    @Override
    public List<WordRandom> findAllBy(QuizCategory quizCategory, long limit) {
        int random = ThreadLocalRandom.current().nextInt(RANDOM_BOUND);
        String quizCategoryName = quizCategory.getName();

        List<WordRandom> result = queryFactory.selectFrom(wordRandom)
                                              .where(wordRandom.random.goe(random), eqCategory(quizCategoryName))
                                              .limit(limit)
                                              .fetch();

        if (result.size() < limit) {
            result.addAll(
                    queryFactory.selectFrom(wordRandom)
                                .where(wordRandom.random.loe(random), eqCategory(quizCategoryName))
                                .limit(limit)
                                .fetch()
            );
        }

        return result.subList(0, (int)(limit));
    }

    private BooleanExpression eqCategory(String categoryName) {
        if (IGNORE_CATEGORY_NAME.equals(categoryName)) {
            return null;
        }

        return wordRandom.category.eq(Category.findBy(categoryName));
    }
}
