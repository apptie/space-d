package com.dnd.spaced.core.word.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.word.application.dto.request.SearchConditionDto;
import com.dnd.spaced.core.word.application.dto.response.ReadAllWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@CleanUpDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordServiceTest {

    @Autowired
    WordService wordService;

    @Autowired
    WordRepository wordRepository;

    @Test
    void read_메서드는_지정한_id에_해당하는_용어_정보를_반환한다() {
        // given
        String name = "word";
        String categoryName = "개발";
        String meaning = "word meaning";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();

        wordRepository.save(word);

        // when
        ReadWordDto actual = wordService.read(word.getId());

        // then
        assertAll(
                () -> assertThat(actual.name()).isEqualTo(name),
                () -> assertThat(actual.categoryName()).isEqualTo(categoryName),
                () -> assertThat(actual.meaning()).isEqualTo(meaning)
        );
    }

    @Test
    void read_메서드는_지정한_id에_해당하는_용어가_없다면_WordNotFoundException_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> wordService.read(-1L))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 용어를 찾을 수 없습니다.");
    }

    @Test
    void readAllBy_메서드는_용어_목록을_조회한다() {
        // given
        String name = "word";
        String categoryName = "개발";
        String meaning = "word meaning";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();

        wordRepository.save(word);

        // when
        List<ReadAllWordDto> actual = wordService.readAllBy(null, null, PageRequest.of(0, 3));

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void search_메서드는_조건을_전달하면_조건에_맞는_Word_목록을_반환한다() {
        // given
        String name = "word";
        String categoryName = "개발";
        String meaning = "word meaning";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();
        Pronunciation pronunciation = new Pronunciation("워드", "한글 발음");

        word.addPronunciation(pronunciation);
        wordRepository.save(word);

        // when
        SearchConditionDto dto = new SearchConditionDto("word", null, null, PageRequest.of(0, 3), null);
        List<SearchedWordDto> actual = wordService.search(dto);

        // then
        assertThat(actual).hasSize(1);
    }
}
