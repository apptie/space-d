package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "today_quizzes")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuiz extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TodayQuizQuestion todayQuizQuestion;

    public TodayQuiz(TodayQuizQuestion todayQuizQuestion) {
        this.todayQuizQuestion = todayQuizQuestion;
    }

    public TodayQuizGradedAnswer grade(Long accountId, SubmitAnswer submitAnswer) {
        return new TodayQuizGradedAnswer(this, accountId, submitAnswer.wordId(), submitAnswer.content());
    }

    public boolean isEqualTo(Long id) {
        return this.id.equals(id);
    }

    boolean isCorrect(Long wordId) {
        return todayQuizQuestion.isCorrect(wordId);
    }

    public record SubmitAnswer(Long wordId, String content) {
    }
}
