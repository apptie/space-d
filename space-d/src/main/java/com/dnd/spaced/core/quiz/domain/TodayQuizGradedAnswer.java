package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuizGradedAnswer extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "today_quiz_id")
    private TodayQuiz todayQuiz;

    private Long accountId;

    private int selectedOptionIndex;

    public TodayQuizGradedAnswer(Long accountId, TodayQuiz todayQuiz, int selectedOptionIndex) {
        this.accountId = accountId;
        this.todayQuiz = todayQuiz;
        this.selectedOptionIndex = selectedOptionIndex;
    }

    public boolean isCorrect() {
        return todayQuiz.isCorrect(selectedOptionIndex);
    }
}
