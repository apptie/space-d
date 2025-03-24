package com.dnd.spaced.core.word.application.event.listener;

import com.dnd.spaced.core.admin.application.enums.WordMetadataCounter;
import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import com.dnd.spaced.core.word.application.event.listener.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WordPersistEventListener {

    private static final long DEFAULT_WORD_METADATA_ID = 1L;

    private final WordRepository wordRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordMetadataRepository wordMetadataRepository;

    @Async("asyncPersistedWordEventListenerExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(PersistedWordEvent event) {
        persistWordRandom(event.wordId(), event.category());
        updateWordMetadata(event.category());
    }

    private void persistWordRandom(Long wordId, Category category) {
        Word word = findWord(wordId);

        wordRandomRepository.saveWith(word, category);
    }

    private void updateWordMetadata(Category category) {
        WordMetadata wordMetadata = findWordMetadata();

        WordMetadataCounter.add(category, wordMetadata);
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new WordNotFoundException("지정한 식별자에 해당하는 용어를 찾을 수 없습니다."));
    }

    private WordMetadata findWordMetadata() {
        return wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                     .orElseThrow(() -> new WordMetadataNotFoundException(
                                             "용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                     );
    }
}
