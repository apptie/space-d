package com.dnd.spaced.core.quiz.domain.embed;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false, of = {"wordId", "content"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizAnswerOption {

    private Long wordId;
    private String content;

    public QuizAnswerOption(Long wordId, String content) {
        this.wordId = wordId;
        this.content = content;
    }

    public boolean matchesWordId(Long wordId) {
        return this.wordId.equals(wordId);
    }
}
