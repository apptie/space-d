package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.word.application.dto.request.SaveWordDto.PronunciationInfoDto;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WordCommandService {

    private final WordRepository wordRepository;
    private final WordExampleRepository wordExampleRepository;
    private final PronunciationRepository pronunciationRepository;

    @Transactional
    public Long saveWord(SaveWordDto saveWordDto) {
        Word word = Word.builder()
                        .name(saveWordDto.name())
                        .meaning(saveWordDto.meaning())
                        .categoryName(saveWordDto.categoryName())
                        .build();

        for (String example : saveWordDto.examples()) {
            WordExample wordExample = new WordExample(example);

            word.addWordExample(wordExample);
        }
        for (PronunciationInfoDto dto : saveWordDto.pronunciations()) {
            Pronunciation pronunciation = new Pronunciation(dto.pronunciation(), dto.typeName());

            word.addPronunciation(pronunciation);
        }

        return wordRepository.save(word)
                             .getId();
    }

    @Transactional
    public void updateWordExample(Long id, String example) {
        wordExampleRepository.update(id, example);
    }

    @Transactional
    public void deleteWordExample(Long id) {
        wordExampleRepository.deleteBy(id);
    }

    @Transactional
    public void deletePronunciation(Long id) {
        pronunciationRepository.deleteBy(id);
    }
}
