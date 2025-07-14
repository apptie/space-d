package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest.CreatePronunciationRequest;
import com.dnd.spaced.core.admin.application.dto.resposne.PersistWordDto;
import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CreateWordService {

    private final WordRepository wordRepository;
    private final WordExampleRepository wordExampleRepository;
    private final PronunciationRepository pronunciationRepository;

    @Transactional
    public PersistWordDto createWord(CreateWordRequest createWordRequest) {
        Word word = setupWord(createWordRequest);

        setupWordExamples(word, createWordRequest);
        setupPronunciations(word, createWordRequest);

        return convertPersistedWordDto(word);
    }

    private Word setupWord(CreateWordRequest createWordRequest) {
        Word word = buildWord(createWordRequest);

        return persistWord(word);
    }

    private Word persistWord(Word word) {
        return wordRepository.save(word);
    }

    private Word buildWord(CreateWordRequest request) {
        return Word.builder()
                   .name(request.name())
                   .meaning(request.meaning())
                   .categoryName(request.categoryName())
                   .build();
    }

    private void setupWordExamples(Word word, CreateWordRequest request) {
        List<WordExample> wordExamples = buildWordExamples(word, request);

        persistWordExamples(wordExamples);
    }

    private List<WordExample> buildWordExamples(Word word, CreateWordRequest request) {
        return request.examples()
                      .stream()
                      .map(example -> buildWordExample(word, example))
                      .toList();
    }

    private WordExample buildWordExample(Word word, String example) {
        WordExample wordExample = WordExample.from(example);

        wordExample.initWord(word);
        return wordExample;
    }

    private void persistWordExamples(List<WordExample> wordExamples) {
        wordExampleRepository.saveAll(wordExamples);
    }

    private void setupPronunciations(Word word, CreateWordRequest request) {
        List<Pronunciation> pronunciations = buildPronunciations(word, request);

        persistPronunciations(pronunciations);
    }

    private void persistPronunciations(List<Pronunciation> pronunciations) {
        pronunciationRepository.saveAll(pronunciations);
    }

    private List<Pronunciation> buildPronunciations(Word word, CreateWordRequest wordRequest) {
        return wordRequest.pronunciations()
                          .stream()
                          .map(pronunciationRequest -> buildPronunciation(word, pronunciationRequest))
                          .toList();
    }

    private Pronunciation buildPronunciation(Word word, CreatePronunciationRequest pronunciationRequest) {
        Pronunciation pronunciation = Pronunciation.of(
                pronunciationRequest.pronunciation(),
                pronunciationRequest.typeName()
        );

        pronunciation.initWord(word);
        return pronunciation;
    }

    private PersistWordDto convertPersistedWordDto(Word word) {
        return new PersistWordDto(word.getId(), word.getCategory());
    }
}
