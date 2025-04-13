package com.dnd.spaced.core.word.domain.dto.mapper;

import static com.dnd.spaced.core.word.domain.dto.WordInfo.PronunciationInfo;

import com.dnd.spaced.core.word.domain.Pronunciation;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.dto.WordInfo;
import com.dnd.spaced.core.word.domain.dto.WordInfo.WordExampleInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WordInfoMapper {

    public static WordInfo toDto(Word word, List<Pronunciation> pronunciations) {
        List<PronunciationInfo> pronunciationInfos = pronunciations.stream()
                                                                   .map(WordInfoMapper::toPronunciationInfoDto)
                                                                   .toList();
        List<WordExampleInfo> wordExampleInfos = word.getWordExamples()
                                                     .stream()
                                                     .map(WordInfoMapper::toWordExampleInfoDto)
                                                     .toList();

        return new WordInfo(
                word.getId(),
                word.getName(),
                word.getCategory(),
                word.getWordMeaning(),
                pronunciationInfos,
                wordExampleInfos,
                word.getViewCount(),
                word.getBookmarkCount()
        );
    }

    private static WordExampleInfo toWordExampleInfoDto(WordExample wordExample) {
        return new WordExampleInfo(
                wordExample.getId(),
                wordExample.getWord().getId(),
                wordExample.getContent()
        );
    }

    private static PronunciationInfo toPronunciationInfoDto(Pronunciation pronunciation) {
        return new PronunciationInfo(
                pronunciation.getId(),
                pronunciation.getWord().getId(),
                pronunciation.getContent(),
                pronunciation.getPronunciationType()
        );
    }
}
