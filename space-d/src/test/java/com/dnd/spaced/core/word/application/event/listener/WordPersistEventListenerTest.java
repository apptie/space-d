package com.dnd.spaced.core.word.application.event.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.spaced.core.admin.application.AdminWordServiceFacade;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.word.application.event.dto.FailedWordPersistedEvent;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordPersistEventListenerTest {

    @Autowired
    WordRepository wordRepository;

    @Autowired
    WordExampleRepository wordExampleRepository;

    @Autowired
    PronunciationRepository pronunciationRepository;

    @Autowired
    AdminWordServiceFacade adminWordServiceFacade;

    @Autowired
    WordRandomRepository wordRandomRepository;

    @Autowired
    RedisTemplate<String, FailedWordPersistedEvent> wordPersistFailedRedisTemplate;

    @Autowired
    ApplicationEvents events;

    @Test
    void 용어_생성_후_정상적으로_용어_생성_이벤트를_수행한다() {
        Word mockWord = mock(Word.class);
        given(mockWord.getId()).willReturn(5L);
        given(mockWord.getCategory()).willReturn(Category.DEVELOP);
        given(wordRepository.findBy(anyLong())).willReturn(Optional.of(mockWord));
        willDoNothing().given(pronunciationRepository).saveAll(any());
        willDoNothing().given(wordExampleRepository).saveAll(any());
        willDoNothing().given(wordRandomRepository).saveWith(any(Word.class), any(Category.class));

        CreatePronunciationRequest createPronunciationRequest = new CreatePronunciationRequest("어싸라이제이션", "한글 발음");
        CreateWordRequest request = new CreateWordRequest(
                "Authorization",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                List.of(createPronunciationRequest),
                List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.")
        );

        adminWordServiceFacade.createWord(request);

        assertAll(
                () -> assertThat(events.stream(PersistedWordEvent.class).count()).isOne(),
                () -> verify(wordRandomRepository).saveWith(any(Word.class), any(Category.class)),
                () -> verify(wordPersistFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    void 용어_생성_후_용어_생성_이벤트_처리에_실패하더라도_최대_재시도_횟수만큼_이벤트_처리를_재시도한다() {
        Word mockWord = mock(Word.class);

        given(mockWord.getId()).willReturn(5L);
        given(mockWord.getCategory()).willReturn(Category.DEVELOP);
        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doReturn(Optional.of(mockWord))
                                                         .when(wordRepository)
                                                         .findBy(anyLong());
        willDoNothing().given(pronunciationRepository).saveAll(any());
        willDoNothing().given(wordExampleRepository).saveAll(any());
        willDoNothing().given(wordRandomRepository).saveWith(any(Word.class), any(Category.class));

        CreatePronunciationRequest createPronunciationRequest = new CreatePronunciationRequest("어싸라이제이션", "한글 발음");
        CreateWordRequest request = new CreateWordRequest(
                "Authorization",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                List.of(createPronunciationRequest),
                List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.")
        );

        adminWordServiceFacade.createWord(request);

        assertAll(
                () -> assertThat(events.stream(PersistedWordEvent.class).count()).isOne(),
                () -> verify(wordRepository, times(3)).findBy(anyLong()),
                () -> verify(wordPersistFailedRedisTemplate, never()).opsForList()
        );
    }

    @Test
    void 용어_생성_후_최대_재시도_횟수보다_더_이벤트_처리에_실패한_횟수가_많다면_실패한_이벤트를_별도로_관리한다() {
        // given
        doThrow(DataAccessResourceFailureException.class).doThrow(DataAccessResourceFailureException.class)
                                                         .doThrow(DataAccessResourceFailureException.class)
                                                         .when(wordRepository)
                                                         .findBy(anyLong());

        CreatePronunciationRequest createPronunciationRequest = new CreatePronunciationRequest("어싸라이제이션", "한글 발음");
        CreateWordRequest request = new CreateWordRequest(
                "Authorization",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                List.of(createPronunciationRequest),
                List.of("게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.")
        );

        adminWordServiceFacade.createWord(request);

        assertAll(
                () -> assertThat(events.stream(PersistedWordEvent.class).count()).isOne(),
                () -> verify(wordRepository, times(3)).findBy(anyLong()),
                () -> verify(wordPersistFailedRedisTemplate).opsForList()
        );
    }
}
