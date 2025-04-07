package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.FailedWordPersistedEvent;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import com.dnd.spaced.core.word.application.event.listener.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.global.consts.LogConst;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class WordPersistEventListener {

    private static final String KEY = "failed-word-persist-event";

    private final Clock clock;
    private final WordRepository wordRepository;
    private final WordRandomRepository wordRandomRepository;
    private final RetryTemplate wordPersistEventRetryTemplate;
    private final WordMetadataRepository wordMetadataRepository;
    private final RedisTemplate<String, FailedWordPersistedEvent> wordPersistFailedRedisTemplate;

    @Async("asyncPersistedWordEventListenerExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void listen(PersistedWordEvent event) {
        String requestId = MDC.get(LogConst.REQUEST_ID);

        try {
            wordPersistEventRetryTemplate.execute(
                    retryContext -> {
                        persistWordRandom(event.wordId(), event.category());
                        updateWordMetadata(event.category());

                        return null;
                    }, retryContext -> {
                        log.error(
                                "[{}] 용어 {} 생성 후 이벤트 처리 실패",
                                requestId,
                                event.wordId(),
                                retryContext.getLastThrowable()
                        );
                        recoverPersistedWordEvent(event);

                        return null;
                    });
        } catch (Throwable e) {
            log.error("[{}] retry 실패", requestId, e);
        }
    }

    private void recoverPersistedWordEvent(PersistedWordEvent event) {
        FailedWordPersistedEvent failedEvent = new FailedWordPersistedEvent(
                event.wordId(),
                event.category().getName(),
                LocalDateTime.now(clock)
        );

        wordPersistFailedRedisTemplate.opsForList()
                                      .rightPush(KEY, failedEvent);
    }

    private void persistWordRandom(Long wordId, Category category) {
        Word word = findWord(wordId);

        wordRandomRepository.saveWith(word, category);
    }

    private void updateWordMetadata(Category category) {
        wordMetadataRepository.update(category);
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new WordNotFoundException("지정한 식별자에 해당하는 용어를 찾을 수 없습니다."));
    }
}
