package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.Word;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordGatewayRepositoryTest {

    @Autowired
    WordGatewayRepository wordGatewayRepository;

    @Autowired
    WordCrudRepository wordCrudRepository;

    @Autowired
    EntityManager em;

    @Test
    void 용어를_영속화_한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        // when
        Word actual = wordGatewayRepository.save(word);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제하지_않은_용어의_영속화_여부를_확인한다() {
        // when
        boolean actual = wordGatewayRepository.existsBy(1L);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제한_용어의_영속화_여부를_확인한다() {
        // when
        boolean actual = wordGatewayRepository.existsBy(2L);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제하지_않은_용어를_조회한다() {
        // when
        Optional<Word> actual = wordGatewayRepository.findBy(1L);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(1L)
        );
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제한_용어는_조회할_수_없다() {
        // when
        Optional<Word> actual = wordGatewayRepository.findBy(2L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_조회_수를_1_증가시킨다() {
        // given
        Word before = wordGatewayRepository.findBy(1L).get();

        assertThat(before.getViewCount()).isZero();

        // when
        wordGatewayRepository.addViewCount(1L);

        // then
        em.clear();

        Word actual = wordGatewayRepository.findBy(1L).get();

        assertThat(actual.getViewCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_조회_수는_증가되지_않는다() {
        // given
        Word before = wordCrudRepository.findById(2L).get();

        assertThat(before.getViewCount()).isZero();

        // when
        wordGatewayRepository.addViewCount(2L);

        // then
        em.clear();

        Word actual = wordGatewayRepository.findBy(1L).get();

        assertThat(actual.getViewCount()).isZero();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_북마크_수를_1_증가시킨다() {
        // given
        Word before = wordGatewayRepository.findBy(1L).get();

        assertThat(before.getBookmarkCount()).isZero();

        // when
        wordGatewayRepository.addBookmarkCount(1L);

        // then
        em.clear();

        Word actual = wordGatewayRepository.findBy(1L).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_북마크_수는_증가되지_않는다() {
        // given
        Word before = wordCrudRepository.findById(2L).get();

        assertThat(before.getBookmarkCount()).isEqualTo(1L);

        // when
        wordGatewayRepository.addBookmarkCount(2L);

        // then
        em.clear();

        Word actual = wordCrudRepository.findById(2L).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_북마크_수를_1_감소시킨다() {
        // given
        wordGatewayRepository.addBookmarkCount(1L);

        Word before = wordGatewayRepository.findBy(1L).get();

        assertThat(before.getBookmarkCount()).isEqualTo(1L);

        // when
        wordGatewayRepository.subtractBookmarkCount(1L);

        // then
        em.clear();

        Word actual = wordGatewayRepository.findBy(1L).get();

        assertThat(actual.getBookmarkCount()).isZero();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_북마크_수는_감소되지_않는다() {
        // given
        Word before = wordCrudRepository.findById(2L).get();

        assertThat(before.getBookmarkCount()).isEqualTo(1L);

        // when
        wordGatewayRepository.subtractBookmarkCount(2L);

        // then
        em.clear();

        Word actual = wordCrudRepository.findById(2L).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제하지_않은_모든_용어의_이름을_조회한다() {
        // when
        List<String> actual = wordGatewayRepository.findNameAllBy(new Long[]{1L, 2L});

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0)).isEqualTo("Authorization")
        );
    }
}
