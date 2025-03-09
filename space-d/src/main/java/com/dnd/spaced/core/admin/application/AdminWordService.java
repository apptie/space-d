package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.enums.WordMetadataCounter;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.UnexpectedUpdateWordExampleCountException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import com.dnd.spaced.core.word.domain.repository.WordRandomRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminWordService {

    private static final long WORD_EXAMPLE_MIN_COUNT = 1L;
    private static final long PRONUNCIATION_MIN_COUNT = 1L;
    private static final long SUCCESS_UPDATE_COUNT = 1L;
    private static final long DEFAULT_WORD_METADATA_ID = 1L;

    private final WordRepository wordRepository;
    private final WordRandomRepository wordRandomRepository;
    private final WordExampleRepository wordExampleRepository;
    private final WordMetadataRepository wordMetadataRepository;
    private final PronunciationRepository pronunciationRepository;

    @Transactional
    public Long createWord(CreateWordRequest createWordRequest) {
        Word word = Word.builder()
                        .name(createWordRequest.name())
                        .meaning(createWordRequest.meaning())
                        .categoryName(createWordRequest.categoryName())
                        .build();

        for (String example : createWordRequest.examples()) {
            WordExample wordExample = new WordExample(example);

            word.addWordExample(wordExample);
        }
        for (CreatePronunciationRequest dto : createWordRequest.pronunciations()) {
            Pronunciation pronunciation = new Pronunciation(dto.pronunciation(), dto.typeName());

            word.addPronunciation(pronunciation);
        }

        Word savedWord = wordRepository.save(word);
        WordMetadata wordMetadata = wordMetadataRepository.findBy(DEFAULT_WORD_METADATA_ID)
                                                          .orElseThrow(() -> new WordMetadataNotFoundException(
                                                                  "용어 메타데이터가 정상적으로 설정되지 않았습니다.")
                                                          );
        WordMetadataCounter.count(word.getCategory(), wordMetadata);
        wordRandomRepository.saveWith(savedWord.getId(), savedWord.getCategory());

        return savedWord.getId();
    }

    @Transactional
    public void updateWordExample(Long id, String example) {
        long updateCount = wordExampleRepository.update(id, example);

        if (updateCount != SUCCESS_UPDATE_COUNT) {
            throw new UnexpectedUpdateWordExampleCountException("용어 예문이 정상적으로 변경되지 않았습니다.");
        }
    }

    @Transactional
    public void deleteWordExample(Long wordId, Long exampleId) {
        if (wordExampleRepository.countBy(wordId) <= WORD_EXAMPLE_MIN_COUNT) {
            throw new WordExampleDeletionNotAllowedException("해당 용어의 예문 개수가 최소치입니다.");
        }

        wordExampleRepository.deleteBy(exampleId);
    }

    @Transactional
    public void deletePronunciation(Long wordId, Long pronunciation) {
        if (pronunciationRepository.countBy(wordId) <= PRONUNCIATION_MIN_COUNT) {
            throw new PronunciationDeletionNotAllowedException("해당 용어의 발음 정보 개수가 최소치입니다.");
        }

        pronunciationRepository.deleteBy(pronunciation);
    }
}
