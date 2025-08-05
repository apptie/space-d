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

    private long submitQuizQuestionCount = 0L;

    private long quizQuestionCorrectCount = 0L;

    private long submitTodayQuizQuestionCount = 0L;

    private long todayQuizQuestionCorrectCount = 0L;

    public Skill(Long accountId) {
        this.accountId = accountId;
    }

    public double calculateQuizQuestionCorrectPercent(long totalQuizQuestionCount) {
        if (hasNeverAttemptedQuiz(totalQuizQuestionCount)) {
            return 0.0d;
        }

        return calculateQuizCorrectPercent(totalQuizQuestionCount);
    }

    public double calculateTodayQuizQuestionCorrectPercent(long totalTodayQuizQuestionCount) {
        if (hasNeverAttemptedTodayQuiz(totalTodayQuizQuestionCount)) {
            return 0.0d;
        }

        return calculateTodayQuizCorrectPercent(totalTodayQuizQuestionCount);
    }

    public void addCorrectQuizQuestion(long correctCount) {
        this.submitQuizQuestionCount += QUIZ_QUESTION_COUNT;
        this.quizQuestionCorrectCount += correctCount;
    }

    public void addCorrectTodayQuizQuestion(long correctCount) {
        this.submitTodayQuizQuestionCount += TODAY_QUIZ_QUESTION_COUNT;
        this.todayQuizQuestionCorrectCount += correctCount;
    }

    private boolean hasNeverAttemptedQuiz(long totalQuizQuestionCount) {
        return totalQuizQuestionCount == 0L || quizQuestionCorrectCount == 0L;
    }

    private double calculateQuizCorrectPercent(long totalQuizQuestionCount) {
        return ((double) quizQuestionCorrectCount / totalQuizQuestionCount) * PERCENT;
    }

    private boolean hasNeverAttemptedTodayQuiz(long totalTodayQuizQuestionCount) {
        return totalTodayQuizQuestionCount == 0L || todayQuizQuestionCorrectCount == 0L;
    }

    private double calculateTodayQuizCorrectPercent(long totalTodayQuizQuestionCount) {
        return ((double) todayQuizQuestionCorrectCount / totalTodayQuizQuestionCount) * PERCENT;
    }
}
