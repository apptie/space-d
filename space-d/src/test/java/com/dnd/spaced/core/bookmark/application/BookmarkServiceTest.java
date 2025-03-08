package com.dnd.spaced.core.bookmark.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.bookmark.application.dto.request.CreateBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.request.ReadAllBookmarkRequest;
import com.dnd.spaced.core.bookmark.application.dto.response.BookmarkCollectionResponse;
import com.dnd.spaced.core.bookmark.application.exception.ForbiddenDeleteBookmarkException;
import com.dnd.spaced.core.bookmark.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountDecrementedEvent;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountIncrementedEvent;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    WordRepository wordRepository;

    @Test
    void 북마크를_추가한다() {
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
        CreateBookmarkRequest request = new CreateBookmarkRequest(word.getId());

        bookmarkService.create(1L, request);

        // then
        assertThat(events.stream(WordBookmarkCountIncrementedEvent.class).count()).isOne();
    }

    @Test
    void 지정한_용어_식별자로_용어를_찾지_못하면_북마크를_추가할_수_없다() {
        // when & then
        CreateBookmarkRequest request = new CreateBookmarkRequest(1L);

        assertThatThrownBy(() -> bookmarkService.create(1L, request))
                .isInstanceOf(WordNotFoundException.class)
                .hasMessage("지정한 식별자의 용어를 찾지 못했습니다.");

    }

    @Test
    void 북마크를_삭제한다() {
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
        bookmarkService.create(1L, new CreateBookmarkRequest(word.getId()));

        // when
        bookmarkService.delete(1L, 1L);

        // then
        assertThat(events.stream(WordBookmarkCountDecrementedEvent.class).count()).isOne();
    }

    @Test
    void 지정한_식별자로_삭제할_북마크를_찾지_못하면_북마크를_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> bookmarkService.delete(1L, 1L));
    }

    @Test
    void 지정한_식별자의_북마크를_작성한_회원이_아니라면_북마크를_삭제할_수_없다() {
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
        bookmarkService.create(1L, new CreateBookmarkRequest(word.getId()));

        // when & then
        assertThatThrownBy(() -> bookmarkService.delete(2L, 1L))
                .isInstanceOf(ForbiddenDeleteBookmarkException.class)
                .hasMessage("북마크 삭제는 생성자만이 가능합니다.");
    }

    @Test
    void 회원이_생성한_북마크를_모두_조회한다() {
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
        bookmarkService.create(1L, new CreateBookmarkRequest(word.getId()));

        // when
        ReadAllBookmarkRequest request = new ReadAllBookmarkRequest(null);

        BookmarkCollectionResponse actual = bookmarkService.findAllBy(1L, request, PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(actual.bookmarks()).hasSize(1),
                () -> assertThat(actual.lastBookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).bookmarkId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).accountId()).isEqualTo(1L),
                () -> assertThat(actual.bookmarks().get(0).wordId()).isEqualTo(word.getId())
        );
    }
}
