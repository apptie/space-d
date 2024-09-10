package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.word.application.event.dto.WordViewCountIncrementEvent;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WordViewCountCalculateEventListener {

    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(WordViewCountIncrementEvent event) {
        try {
            if (!popularWordRepository.existsBy(event.wordId(), event.localDateTime())) {
                wordRepository.updateViewCount(event.wordId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
