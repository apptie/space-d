package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidWordMeaningException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordMeaning {

    private static final int MIN_MEANING_LENGTH = 10;
    private static final int MAX_MEANING_LENGTH = 70;

    private String meaning;

    public WordMeaning(String meaning) {
        validateContent(meaning);

        this.meaning = meaning;
    }

    public void changeMeaning(String meaning) {
        validateContent(meaning);

        this.meaning = meaning;
    }

    private void validateContent(String meaning) {
        if (isInvalidMeaning(meaning)) {
            throw new InvalidWordMeaningException("용어 뜻은 최소 10글자 이상, 최대 70글자 이하여야 합니다.");
        }
    }

    private boolean isInvalidMeaning(String meaning) {
        return meaning == null || meaning.isBlank()
                || MIN_MEANING_LENGTH > meaning.length() || MAX_MEANING_LENGTH < meaning.length();
    }
}
