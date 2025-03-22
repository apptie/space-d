package com.dnd.spaced.core.quiz.domain.embed;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false, of = {"answerWordId", "answerContent"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuizAnswerOption {

    private Long answerWordId;
    private String answerContent;

    public TodayQuizAnswerOption(Long answerWordId, String answerContent) {
        this.answerWordId = answerWordId;
        this.answerContent = answerContent;
    }

    public boolean matchesWordId(Long wordId) {
        return this.answerWordId.equals(wordId);
    }
}
