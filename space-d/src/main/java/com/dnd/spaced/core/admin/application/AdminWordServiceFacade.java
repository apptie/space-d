package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.PersistWordDto;
import com.dnd.spaced.core.admin.application.event.dto.DeletedWordEvent;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminWordServiceFacade {

    private final CreateWordService createWordservice;
    private final UpdateWordService updateWordService;
    private final DeleteWordService deleteWordService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createWord(CreateWordRequest createWordRequest) {
        PersistWordDto wordDto = createWordservice.createWord(createWordRequest);

        publishPersistedWordEvent(wordDto);
        return wordDto.id();
    }

    @Transactional
    public void updateWordExample(Long wordExampleId, String example) {
        updateWordService.updateWordExample(wordExampleId, example);
    }

    @Transactional
    public void deleteWordExample(Long wordId, Long wordExampleId) {
        deleteWordService.deleteWordExample(wordId, wordExampleId);
    }

    @Transactional
    public void deletePronunciation(Long wordId, Long pronunciationId) {
        deleteWordService.deletePronunciation(wordId, pronunciationId);
    }

    @Transactional
    public void deleteWord(Long wordId) {
        deleteWordService.deleteWord(wordId);

        publishDeletedWordEvent(wordId);
    }

    private void publishDeletedWordEvent(Long wordId) {
        eventPublisher.publishEvent(new DeletedWordEvent(wordId));
    }

    private void publishPersistedWordEvent(PersistWordDto wordDto) {
        eventPublisher.publishEvent(new PersistedWordEvent(wordDto.id(), wordDto.category()));
    }
}
