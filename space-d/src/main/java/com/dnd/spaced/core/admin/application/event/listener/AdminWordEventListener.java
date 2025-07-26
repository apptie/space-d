package com.dnd.spaced.core.admin.application.event.listener;

import com.dnd.spaced.core.admin.application.event.dto.DeletedWordEvent;
import com.dnd.spaced.core.word.application.repository.DeletedWordIdRepository;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminWordEventListener {

    private final Clock clock;
    private final WordExampleRepository wordExampleRepository;
    private final DeletedWordIdRepository deletedWordIdRepository;
    private final PronunciationRepository pronunciationRepository;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void listen(DeletedWordEvent event) {
        deletedWordIdRepository.save(event.wordId(), LocalDateTime.now(clock));
        wordExampleRepository.deleteAllBy(event.wordId());
        pronunciationRepository.deleteAllBy(event.wordId());
    }
}
