package com.dnd.spaced.core.skill.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "skills")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {

    private static final int QUIZ_QUESTION_COUNT = 5;
    private static final int TODAY_QUIZ_QUESTION_COUNT = 1;
    private static final double PERCENT = 100d;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private long submitQuizQuestionCount = 0;

    private long quizQuestionCorrectCount = 0;

    private long submitTodayQuizQuestionCount = 0;

    private long todayQuizQuestionCorrectCount = 0;

    public Skill(Long accountId) {
        this.accountId = accountId;
    }

    public double calculateQuizQuestionCorrectPercent(long totalQuizQuestionCount) {
        if (totalQuizQuestionCount == 0L || quizQuestionCorrectCount == 0L) {
            return 0.0d;
        }

        return ((double) quizQuestionCorrectCount / totalQuizQuestionCount) * PERCENT;
    }

    public double calculateTodayQuizQuestionCorrectPercent(long totalTodayQuizQuestionCount) {
        if (totalTodayQuizQuestionCount == 0L || todayQuizQuestionCorrectCount == 0L) {
            return 0.0d;
        }

        return ((double) todayQuizQuestionCorrectCount / totalTodayQuizQuestionCount) * PERCENT;
    }

    public void addCorrectQuizQuestion(long correctCount) {
        this.submitQuizQuestionCount += QUIZ_QUESTION_COUNT;
        this.quizQuestionCorrectCount += correctCount;
    }

    public void addCorrectTodayQuizQuestion(long correctCount) {
        this.submitTodayQuizQuestionCount += TODAY_QUIZ_QUESTION_COUNT;
        this.todayQuizQuestionCorrectCount += correctCount;
    }
}
