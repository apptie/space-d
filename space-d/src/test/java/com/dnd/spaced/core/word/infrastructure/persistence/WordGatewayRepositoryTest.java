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

    private static final long WORD_ID = 1L;
    private static final long DELETED_WORD_ID = 2L;

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
        boolean actual = wordGatewayRepository.existsBy(WORD_ID);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제한_용어의_영속화_여부를_확인한다() {
        // when
        boolean actual = wordGatewayRepository.existsBy(DELETED_WORD_ID);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제하지_않은_용어를_조회한다() {
        // when
        Optional<Word> actual = wordGatewayRepository.findBy(WORD_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(WORD_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제한_용어는_조회할_수_없다() {
        // when
        Optional<Word> actual = wordGatewayRepository.findBy(DELETED_WORD_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_조회_수를_1_증가시킨다() {
        // given
        Word word = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(word.getViewCount()).isZero();

        em.clear();

        // when
        wordGatewayRepository.addViewCount(WORD_ID);

        // then
        Word actual = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(actual.getViewCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_조회_수는_증가되지_않는다() {
        // given
        Word deletedWord = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(deletedWord.getViewCount()).isZero();

        em.clear();

        // when
        wordGatewayRepository.addViewCount(DELETED_WORD_ID);

        // then
        Word actual = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(actual.getViewCount()).isZero();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_북마크_수를_1_증가시킨다() {
        // given
        Word word = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(word.getBookmarkCount()).isZero();

        em.clear();

        // when
        wordGatewayRepository.addBookmarkCount(WORD_ID);

        // then
        Word actual = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_북마크_수는_증가되지_않는다() {
        // given
        Word deletedWord = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(deletedWord.getBookmarkCount()).isEqualTo(1L);

        em.clear();

        // when
        wordGatewayRepository.addBookmarkCount(DELETED_WORD_ID);

        // then
        Word actual = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제하지_않은_용어의_북마크_수를_1_감소시킨다() {
        // given
        wordGatewayRepository.addBookmarkCount(WORD_ID);

        Word word = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(word.getBookmarkCount()).isEqualTo(1L);

        em.clear();

        // when
        wordGatewayRepository.subtractBookmarkCount(WORD_ID);

        // then
        Word actual = wordGatewayRepository.findBy(WORD_ID).get();

        assertThat(actual.getBookmarkCount()).isZero();
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    @Transactional
    void 삭제한_용어의_북마크_수는_감소되지_않는다() {
        // given
        Word deletedWord = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(deletedWord.getBookmarkCount()).isEqualTo(1L);

        em.clear();

        // when
        wordGatewayRepository.subtractBookmarkCount(DELETED_WORD_ID);

        // then
        Word actual = wordCrudRepository.findById(DELETED_WORD_ID).get();

        assertThat(actual.getBookmarkCount()).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word.sql")
    void 삭제하지_않은_모든_용어의_이름을_조회한다() {
        // when
        List<String> actual = wordGatewayRepository.findNameAllBy(new Long[]{WORD_ID, DELETED_WORD_ID});

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0)).isEqualTo("Authorization")
        );
    }
}
