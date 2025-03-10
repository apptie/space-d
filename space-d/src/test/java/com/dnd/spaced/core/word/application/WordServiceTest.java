package com.dnd.spaced.core.word.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpPersistence;
import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.dto.PopularWord;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpPersistence
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordServiceTest {

    @Autowired
    WordService wordService;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    PopularWordRepository popularWordRepository;

    @Autowired
    ApplicationEvents events;

    @Test
    void 용어를_조회한다() {
        // given
        String name = "Authorization";
        String categoryName = "개발";
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();

        wordRepository.save(word);

        // when
        WordResponse actual = wordService.readWord(word.getId());

        // then
        assertAll(
                () -> assertThat(actual.name()).isEqualTo(name),
                () -> assertThat(actual.category()).isEqualTo(categoryName),
                () -> assertThat(actual.meaning()).isEqualTo(meaning),
                () -> assertThat(events.stream(WordViewCountIncrementEvent.class).count()).isOne(),
                () -> assertThat(events.stream(WordViewCountStatisticsEvent.class).count()).isOne()
        );
    }

    @Test
    void 용어_식별자로_용어를_찾지_못하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> wordService.readWord(-1L))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 ID에 해당하는 용어를 찾을 수 없습니다.");
    }

    @Test
    void 용어_목록을_조회한다() {
        // given
        String name = "Authorization";
        String categoryName = "개발";
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();

        wordRepository.save(word);

        ReadAllWordRequest request = new ReadAllWordRequest(null, null);

        // when
        WordCollectionResponse actual = wordService.readWords(request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual.words()).hasSize(1),
                () -> assertThat(actual.lastWordName()).isEqualTo("Authorization")
        );
    }

    @Test
    void 용어를_검색한다() {
        // given
        String name = "Authorization";
        String categoryName = "개발";
        String meaning = "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘";
        Word word = Word.builder()
                        .name(name)
                        .categoryName(categoryName)
                        .meaning(meaning)
                        .build();
        Pronunciation pronunciation = new Pronunciation("어써라이제이션", "한글 발음");

        word.addPronunciation(pronunciation);
        wordRepository.save(word);

        SearchWordRequest request = new SearchWordRequest("Authorization", null, null, null);

        // when
        WordCollectionResponse actual = wordService.searchWord(request, Pageable.ofSize(10));

        // then
        assertAll(
                () -> assertThat(actual.words()).hasSize(1),
                () -> assertThat(actual.lastWordName()).isEqualTo("Authorization")
        );
    }

    @Test
    void 많이_찾아본_용어_목록을_조회한다() {
        // given
        PopularWord popularWord = new PopularWord(1, 1L, "Authorization");
        popularWordRepository.saveAll(List.of(popularWord), LocalDateTime.now());

        // when
        PopularWordCollectionResponse actual = wordService.readPopularWords();

        // then
        assertAll(
                () -> assertThat(actual.popularWords()).hasSize(1),
                () -> assertThat(actual.popularWords().get(0).rank()).isEqualTo(popularWord.rank()),
                () -> assertThat(actual.popularWords().get(0).name()).isEqualTo(popularWord.name()),
                () -> assertThat(actual.popularWords().get(0).wordId()).isEqualTo(popularWord.wordId())
        );
    }
}
