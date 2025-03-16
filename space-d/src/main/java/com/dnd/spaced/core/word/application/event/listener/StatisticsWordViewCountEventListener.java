package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.domain.repository.WordViewCountStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsWordViewCountEventListener {

    private static final String REQUEST_ID = "request_id";

    private final WordViewCountStatisticsRepository wordViewCountStatisticsRepository;

    @Async("asyncStatisticsWordViewCountExecutor")
    @EventListener
    public void listen(WordViewCountStatisticsEvent event) {
        wordViewCountStatisticsRepository.save(event.wordId(), event.localDateTime());
        String requestId = MDC.get(REQUEST_ID);
        log.info("[{}] wordId : {}, localDateTime : {}", requestId, event.wordId(), event.localDateTime());
    }
}
