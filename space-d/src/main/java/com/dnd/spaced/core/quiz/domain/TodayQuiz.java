package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.embed.TodayQuizQuestion;
import com.dnd.spaced.core.quiz.domain.exception.InvalidSubmittedTodayQuizOptionIndexException;
import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
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

    void initTodayQuizOption(TodayQuizOption todayQuizOption) {
        todayQuizQuestion.initTodayQuizOption(todayQuizOption);
    }

    public TodayQuizGradedAnswer grade(Long accountId, int submitOptionIndex) {
        validateSubmitOptionIndex(submitOptionIndex);

        return new TodayQuizGradedAnswer(accountId, this, submitOptionIndex);
    }

    boolean isCorrect(int submitOptionIndex) {
        validateSubmitOptionIndex(submitOptionIndex);

        return todayQuizQuestion.isCorrect(submitOptionIndex);
    }

    private void validateSubmitOptionIndex(int submitOptionIndex) {
        if (todayQuizQuestion.isInvalidOptionIndex(submitOptionIndex)) {
            throw new InvalidSubmittedTodayQuizOptionIndexException("없는 보기를 선택했습니다.");
        }
    }

    public List<TodayQuizOption> getTodayQuizOptions() {
        return todayQuizQuestion.getTodayQuizOptions();
    }
}
