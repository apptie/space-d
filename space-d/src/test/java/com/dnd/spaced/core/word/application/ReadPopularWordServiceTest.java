package com.dnd.spaced.core.word.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.dto.PopularWord;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReadPopularWordServiceTest {

    private static final Long WORD_ID = 1L;

    @Autowired
    ReadPopularWordService readPopularWordService;

    @Autowired
    PopularWordRepository popularWordRepository;

    @Test
    void 많이_찾아본_용어_목록을_조회한다() {
        // given
        PopularWord popularWord = new PopularWord(1, WORD_ID, "Authorization");
        popularWordRepository.saveAll(List.of(popularWord), LocalDateTime.now());

        // when
        List<PopularWord> actual = readPopularWordService.readPopularWords();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).rank()).isEqualTo(popularWord.rank()),
                () -> assertThat(actual.get(0).name()).isEqualTo(popularWord.name()),
                () -> assertThat(actual.get(0).wordId()).isEqualTo(popularWord.wordId())
        );
    }
}
