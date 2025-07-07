package com.dnd.spaced.core.word.domain.dto.mapper;

import static com.dnd.spaced.core.word.domain.dto.WordView.PronunciationView;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.dto.WordView.WordExampleView;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WordViewMapper {

    public static WordView toDto(Word word, List<Pronunciation> pronunciations) {
        List<PronunciationView> pronunciationViews = pronunciations.stream()
                                                                   .map(WordViewMapper::toPronunciationInfoDto)
                                                                   .toList();
        List<WordExampleView> wordExampleViews = word.getWordExamples()
                                                     .stream()
                                                     .map(WordViewMapper::toWordExampleInfoDto)
                                                     .toList();

        return new WordView(
                word.getId(),
                word.getName(),
                word.getCategory(),
                word.getWordMeaning(),
                pronunciationViews,
                wordExampleViews,
                word.getViewCount(),
                word.getBookmarkCount()
        );
    }

    private static WordExampleView toWordExampleInfoDto(WordExample wordExample) {
        return new WordExampleView(
                wordExample.getId(),
                wordExample.getWord().getId(),
                wordExample.getContent()
        );
    }

    private static PronunciationView toPronunciationInfoDto(Pronunciation pronunciation) {
        return new PronunciationView(
                pronunciation.getId(),
                pronunciation.getWord().getId(),
                pronunciation.getContent(),
                pronunciation.getPronunciationType()
        );
    }
}
