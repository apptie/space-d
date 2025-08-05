package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
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
class PronunciationGatewayRepositoryTest {

    private static final long PRONUNCIATION_ID = 1L;
    private static final long DELETED_PRONUNCIATION_ID = 2L;
    private static final long WORD_ID = 1L;

    @Autowired
    PronunciationGatewayRepository pronunciationRepository;

    @Autowired
    WordRepository wordRepository;

    @Test
    @Sql("classpath:sql/word/pronunciation.sql")
    void 삭제하지_않은_용어_발음을_id로_조회한다() {
        // when
        Optional<Pronunciation> actual = pronunciationRepository.findBy(PRONUNCIATION_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(PRONUNCIATION_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/pronunciation.sql")
    void 삭제한_용어_발음은_id로_조회할_수_없다() {
        // when
        Optional<Pronunciation> actual = pronunciationRepository.findBy(DELETED_PRONUNCIATION_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 용어_발음_다수를_영속화_한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        wordRepository.save(word);

        Pronunciation pronunciation1 = Pronunciation.of("어써라이제이션", "한글 발음");
        Pronunciation pronunciation2 = Pronunciation.of("어썰라이제이션", "한글 발음");
        Pronunciation pronunciation3 = Pronunciation.of("오써러제이션", "한글 발음");
        pronunciation1.initWord(word);
        pronunciation2.initWord(word);
        pronunciation3.initWord(word);

        List<Pronunciation> pronunciations = List.of(pronunciation1, pronunciation2, pronunciation3);

        // when
        pronunciationRepository.saveAll(pronunciations);

        // then
        Optional<Pronunciation> actual1 = pronunciationRepository.findBy(1L);
        Optional<Pronunciation> actual2 = pronunciationRepository.findBy(2L);
        Optional<Pronunciation> actual3 = pronunciationRepository.findBy(3L);

        assertAll(
                () -> assertThat(actual1.get().getContent()).isEqualTo("어써라이제이션"),
                () -> assertThat(actual2.get().getContent()).isEqualTo("어썰라이제이션"),
                () -> assertThat(actual3.get().getContent()).isEqualTo("오써러제이션")
        );
    }

    @Test
    @Sql("classpath:sql/word/pronunciation.sql")
    void 삭제되지_않은_발음_개수를_조회한다() {
        // when
        long actual = pronunciationRepository.countBy(WORD_ID);

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/pronunciation.sql")
    @Transactional
    void 특정_용어의_모든_용어_발음을_삭제한다() {
        // when
        pronunciationRepository.deleteAllBy(1L);

        // then
        long actual = pronunciationRepository.countBy(WORD_ID);

        assertThat(actual).isZero();
    }
}
