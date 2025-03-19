package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradedAnswer extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private Long quizId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_question_id")
    private QuizQuestion quizQuestion;

    private Long selectedWordId;
    private String selectedContent;

    public static GradedAnswer of(
            Long accountId,
            Long quizId,
            QuizQuestion quizQuestion,
            Long selectedWordId,
            String selectedContent
    ) {
        return new GradedAnswer(accountId, quizId, quizQuestion, selectedWordId, selectedContent);
    }

    private GradedAnswer(
            Long accountId,
            Long quizId,
            QuizQuestion quizQuestion,
            Long selectedWordId,
            String selectedContent
    ) {
        this.accountId = accountId;
        this.quizId = quizId;
        this.quizQuestion = quizQuestion;
        this.selectedWordId = selectedWordId;
        this.selectedContent = selectedContent;
    }

    public boolean isCorrect() {
        return quizQuestion.isCorrect(selectedWordId);
    }
}
