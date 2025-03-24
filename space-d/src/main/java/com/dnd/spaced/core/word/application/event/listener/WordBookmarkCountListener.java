package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountDecrementedEvent;
import com.dnd.spaced.core.word.application.event.dto.WordBookmarkCountIncrementedEvent;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WordBookmarkCountListener {

    private final WordRepository wordRepository;

    @Async("asyncBookmarkCounterExecutor")
    @EventListener
    @Transactional
    public void listen(WordBookmarkCountIncrementedEvent event) {
        wordRepository.addBookmarkCount(event.wordId());
    }

    @Async("asyncBookmarkCounterExecutor")
    @EventListener
    @Transactional
    public void listen(WordBookmarkCountDecrementedEvent event) {
        wordRepository.updateSubtractBookmarkCount(event.wordId());
    }
}
