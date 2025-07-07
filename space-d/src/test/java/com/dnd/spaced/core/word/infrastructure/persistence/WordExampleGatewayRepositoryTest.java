package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
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
class WordExampleGatewayRepositoryTest {

    @Autowired
    WordExampleGatewayRepository wordExampleRepository;

    @Autowired
    WordRepository wordRepository;

    @Test
    @Sql("classpath:sql/word/word_example.sql")
    void 삭제하지_않은_용어_예문을_조회한다() {
        // when
        Optional<WordExample> actual = wordExampleRepository.findBy(1L);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(1L)
        );
    }

    @Test
    @Sql("classpath:sql/word/word_example.sql")
    void 삭제한_용어_예문은_조회할_수_없다() {
        // when
        Optional<WordExample> actual = wordExampleRepository.findBy(2L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/word/word_example.sql")
    void 삭제하지_않은_용어_예문_개수를_조회한다() {
        // when
        long actual = wordExampleRepository.countBy(1L);

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    @Sql("classpath:sql/word/word_example.sql")
    @Transactional
    void 삭제하지_않은_용어_예문을_수정한다() {
        // when
        wordExampleRepository.update(1L, "웹 API 요청 시 사용자 인증을 위해서는 HTTP 헤더에 Authorization 토큰을 포함해야 합니다. 서버는 이 토큰을 검증하여 접근 권한을 확인한 후 요청된 데이터를 반환합니다.");

        // then
        WordExample actual = wordExampleRepository.findBy(1L).get();

        assertThat(actual.getContent()).isEqualTo("웹 API 요청 시 사용자 인증을 위해서는 HTTP 헤더에 Authorization 토큰을 포함해야 합니다. 서버는 이 토큰을 검증하여 접근 권한을 확인한 후 요청된 데이터를 반환합니다.");
    }

    @Test
    @Sql("classpath:sql/word/word_example.sql")
    @Transactional
    void 특정_용어의_모든_용어_예문을_삭제한다() {
        // when
        wordExampleRepository.deleteAllBy(1L);

        // then
        long actual = wordExampleRepository.countBy(1L);

        assertThat(actual).isZero();
    }

    @Test
    void 용어_예문_다수를_영속화한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();

        wordRepository.save(word);

        WordExample wordExample1 = WordExample.from("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.");
        WordExample wordExample2 = WordExample.from("해당 에러는 Authorization 과정이 실패해서 발생했습니다");
        WordExample wordExample3 = WordExample.from("웹 API 요청 시 사용자 인증을 위해서는 HTTP 헤더에 Authorization 토큰을 포함해야 합니다. 서버는 이 토큰을 검증하여 접근 권한을 확인한 후 요청된 데이터를 반환합니다.");
        wordExample1.initWord(word);
        wordExample2.initWord(word);
        wordExample3.initWord(word);

        List<WordExample> wordExamples = List.of(wordExample1, wordExample2, wordExample3);

        // when
        wordExampleRepository.saveAll(wordExamples);

        // then
        WordExample actual1 = wordExampleRepository.findBy(1L).get();
        WordExample actual2 = wordExampleRepository.findBy(2L).get();
        WordExample actual3 = wordExampleRepository.findBy(3L).get();

        assertAll(
                () -> assertThat(actual1.getContent()).isEqualTo(wordExample1.getContent()),
                () -> assertThat(actual2.getContent()).isEqualTo(wordExample2.getContent()),
                () -> assertThat(actual3.getContent()).isEqualTo(wordExample3.getContent())
        );
    }
}
