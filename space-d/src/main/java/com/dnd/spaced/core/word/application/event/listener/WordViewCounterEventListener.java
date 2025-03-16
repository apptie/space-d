package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class WordViewCounterEventListener {

    private static final String REQUEST_ID = "request_id";

    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;
    private final TransactionTemplate transactionTemplate;

    @Async("asyncWordViewCounterExecutor")
    @EventListener
    public void listen(WordViewCountIncrementEvent event) {
        if (!popularWordRepository.existsBy(event.wordId(), event.localDateTime())) {
            transactionTemplate.executeWithoutResult(status -> wordRepository.updateViewCount(event.wordId()));
            String requestId = MDC.get(REQUEST_ID);
            log.info("[{}] wordId : {}, localDateTime : {}", requestId, event.wordId(), event.localDateTime());
        }
    }
}
