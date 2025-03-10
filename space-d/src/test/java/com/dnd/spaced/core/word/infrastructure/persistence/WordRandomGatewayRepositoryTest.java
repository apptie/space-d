package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.Category;
import com.dnd.spaced.core.word.domain.WordRandom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@CleanUpDatabase
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordRandomGatewayRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    WordRandomCrudRepository wordRandomCrudRepository;

    WordRandomGatewayRepository wordRandomGatewayRepository;

    @BeforeEach
    void beforeEach() {
        wordRandomGatewayRepository = new WordRandomGatewayRepository(
                wordRandomCrudRepository,
                new JPAQueryFactory(em)
        );
    }

    @Test
    void 용어_랜덤값을_저장한다() {
        // when
        wordRandomGatewayRepository.saveWith(1L, Category.DEVELOP);

        // then
        Optional<WordRandom> actual = wordRandomCrudRepository.findById(1L);

        assertAll(
                () -> assertThat(actual.get().getId()).isPositive(),
                () -> assertThat(actual.get().getWordId()).isEqualTo(1L),
                () -> assertThat(actual.get().getCategory()).isEqualTo(Category.DEVELOP),
                () -> assertThat(actual.get().getRandom()).isPositive()
        );
    }

    @Test
    void 동일한_카테고리인_용어_랜덤값을_전달한_limit만큼_조회한다() {
        // given
        for (long i = 1L; i <= 20L; i++) {
            wordRandomGatewayRepository.saveWith(i, Category.DEVELOP);
        }

        // when
        List<WordRandom> actual = wordRandomGatewayRepository.findAllBy(QuizCategory.DEVELOP, 20L);

        // then
        assertThat(actual).hasSize(20);
    }

    @Test
    void 여러_카테고리인_용어_랜덤값을_전달한_limit만큼_조회한다() {
        // given
        for (long i = 1L; i <= 5L; i++) {
            wordRandomGatewayRepository.saveWith(i, Category.DEVELOP);
        }

        for (long i = 6L; i <= 15L; i++) {
            wordRandomGatewayRepository.saveWith(i, Category.DESIGN);
        }

        for (long i = 16L; i <= 20L; i++) {
            wordRandomGatewayRepository.saveWith(i, Category.BUSINESS);
        }

        // when
        List<WordRandom> actual = wordRandomGatewayRepository.findAllBy(QuizCategory.TOTAL, 20L);

        // then
        assertThat(actual).hasSize(20);
    }
}
