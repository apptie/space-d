package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.PronunciationNotFoundException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import com.dnd.spaced.core.word.application.exception.WordNotFoundException;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class DeleteWordService {

    private static final long WORD_EXAMPLE_MIN_COUNT = 1L;
    private static final long PRONUNCIATION_MIN_COUNT = 1L;

    private final WordRepository wordRepository;
    private final WordExampleRepository wordExampleRepository;
    private final PronunciationRepository pronunciationRepository;

    @Transactional
    public void deleteWord(Long wordId) {
        Word word = findWord(wordId);

        executeWordDeletion(word);
    }

    @Transactional
    public void deleteWordExample(Long wordId, Long wordExampleId) {
        WordExample wordExample = findWordExample(wordExampleId);

        validateWordExampleCount(wordId);
        executeWordExampleDeletion(wordExample);
    }

    @Transactional
    public void deletePronunciation(Long wordId, Long pronunciationId) {
        Pronunciation pronunciation = findPronunciation(pronunciationId);

        validatePronunciationCount(wordId);
        executePronunciationDeletion(pronunciation);
    }

    private Word findWord(Long wordId) {
        return wordRepository.findBy(wordId)
                             .orElseThrow(() -> new WordNotFoundException("지정한 용어를 찾을 수 없습니다."));
    }

    private WordExample findWordExample(Long wordExampleId) {
        return wordExampleRepository.findBy(wordExampleId)
                                    .orElseThrow(() -> new WordExampleNotFoundException(
                                            "지정한 용어 예문을 찾을 수 없습니다.")
                                    );
    }

    private Pronunciation findPronunciation(Long pronunciationId) {
        return pronunciationRepository.findBy(pronunciationId)
                                      .orElseThrow(() -> new PronunciationNotFoundException(
                                              "지정한 발음을 찾지 못했습니다.")
                                      );
    }

    private void validateWordExampleCount(Long wordId) {
        if (wordExampleRepository.countBy(wordId) <= WORD_EXAMPLE_MIN_COUNT) {
            throw new WordExampleDeletionNotAllowedException("해당 용어의 예문 개수가 최소치입니다.");
        }
    }

    private void validatePronunciationCount(Long wordId) {
        if (pronunciationRepository.countBy(wordId) <= PRONUNCIATION_MIN_COUNT) {
            throw new PronunciationDeletionNotAllowedException("해당 용어의 발음 정보 개수가 최소치입니다.");
        }
    }

    private void executeWordDeletion(Word word) {
        word.delete();
    }

    private void executeWordExampleDeletion(WordExample wordExample) {
        wordExample.deleted();
    }

    private void executePronunciationDeletion(Pronunciation pronunciation) {
        pronunciation.deleted();
    }
}
