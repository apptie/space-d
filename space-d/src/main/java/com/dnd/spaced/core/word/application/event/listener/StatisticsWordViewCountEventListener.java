package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.WordViewCountStatisticsEvent;
import com.dnd.spaced.core.word.domain.repository.WordViewCountStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StatisticsWordViewCountEventListener {

    private final WordViewCountStatisticsRepository wordViewCountStatisticsRepository;

    @Async("asyncWordViewCountExecutor")
    @Transactional
    @EventListener
    public void listen(WordViewCountStatisticsEvent event) {
        wordViewCountStatisticsRepository.save(event.wordId(), event.localDateTime());
    }
}
