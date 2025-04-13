package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.exception.PronunciationDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.PronunciationNotFoundException;
import com.dnd.spaced.core.admin.application.exception.WordExampleDeletionNotAllowedException;
import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import com.dnd.spaced.core.word.application.event.dto.PersistedWordEvent;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminWordService {

    private static final long WORD_EXAMPLE_MIN_COUNT = 1L;
    private static final long PRONUNCIATION_MIN_COUNT = 1L;

    private final WordRepository wordRepository;
    private final WordExampleRepository wordExampleRepository;
    private final PronunciationRepository pronunciationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createWord(CreateWordRequest createWordRequest) {
        Word word = buildWordFromRequest(createWordRequest);
        Word savedWord = wordRepository.save(word);

        persistExamples(savedWord, createWordRequest);
        persistPronunciations(savedWord, createWordRequest);
        publishPersistedEvent(savedWord);

        return savedWord.getId();
    }

    @Transactional
    public void updateWordExample(Long wordExampleId, String example) {
        WordExample wordExample = findWordExample(wordExampleId);

        wordExample.changeExample(example);
    }

    @Transactional
    public void deleteWordExample(Long wordId, Long wordExampleId) {
        WordExample wordExample = findWordExample(wordExampleId);

        validateExampleCount(wordId);
        wordExample.deleted();
    }

    @Transactional
    public void deletePronunciation(Long wordId, Long pronunciationId) {
        Pronunciation pronunciation = findPronunciation(pronunciationId);

        validatePronunciationCount(wordId);

        pronunciation.deleted();
    }

    private Word buildWordFromRequest(CreateWordRequest request) {
        return Word.builder()
                   .name(request.name())
                   .meaning(request.meaning())
                   .categoryName(request.categoryName())
                   .build();
    }

    private void persistExamples(Word word, CreateWordRequest request) {
        List<WordExample> wordExamples = new ArrayList<>();

        for (String example : request.examples()) {
            WordExample wordExample = WordExample.from(example);

            wordExample.initWord(word);
            wordExamples.add(wordExample);
        }

        wordExampleRepository.saveAll(wordExamples);
    }

    private void persistPronunciations(Word word, CreateWordRequest request) {
        List<Pronunciation> pronunciations = new ArrayList<>();

        for (CreatePronunciationRequest pronunciationInfo : request.pronunciations()) {
            Pronunciation pronunciation = Pronunciation.of(
                    pronunciationInfo.pronunciation(),
                    pronunciationInfo.typeName()
            );

            pronunciation.initWord(word);
            pronunciations.add(pronunciation);
        }

        pronunciationRepository.saveAll(pronunciations);
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

    private void validateExampleCount(Long wordId) {
        if (wordExampleRepository.countBy(wordId) <= WORD_EXAMPLE_MIN_COUNT) {
            throw new WordExampleDeletionNotAllowedException("해당 용어의 예문 개수가 최소치입니다.");
        }
    }

    private void validatePronunciationCount(Long wordId) {
        if (pronunciationRepository.countBy(wordId) <= PRONUNCIATION_MIN_COUNT) {
            throw new PronunciationDeletionNotAllowedException("해당 용어의 발음 정보 개수가 최소치입니다.");
        }
    }

    private void publishPersistedEvent(Word word) {
        eventPublisher.publishEvent(new PersistedWordEvent(word.getId(), word.getCategory()));
    }
}
