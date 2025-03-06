package com.dnd.spaced.core.quiz.domain.embed;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidSubmittedTodayQuizOptionIndexException;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizExampleContentException;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizQuestionException;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuizQuestion {

    private static final int OPTION_SIZE = 4;

    @Enumerated(EnumType.STRING)
    private QuizCategory quizCategory;

    private String question;

    private String questionContent;

    private TodayQuizAnswerOption todayQuizAnswerOption;

    @OneToMany(mappedBy = "todayQuiz", cascade = CascadeType.REMOVE)
    @Getter(AccessLevel.NONE)
    private List<TodayQuizOption> todayQuizOptions = new ArrayList<>();

    public static TodayQuizQuestion of(
            QuizCategory quizCategory,
            String questionContent,
            String questionExample,
            TodayQuizAnswerOption quizAnswerOption
    ) {
        validateContent(questionContent, questionExample);

        return new TodayQuizQuestion(quizCategory, questionContent, questionExample, quizAnswerOption);
    }

    private static void validateContent(String questionContent, String exampleContent) {
        if (questionContent == null || questionContent.isBlank()) {
            throw new InvalidTodayQuizQuestionException("유효한 길이의 퀴즈 질문이 아닙니다.");
        }

        if (exampleContent == null || exampleContent.isBlank()) {
            throw new InvalidTodayQuizExampleContentException("유효한 길이의 퀴즈 지문이 아닙니다.");
        }
    }

    private TodayQuizQuestion(
            QuizCategory quizCategory,
            String question,
            String questionContent,
            TodayQuizAnswerOption todayQuizAnswerOption
    ) {
        this.quizCategory = quizCategory;
        this.question = question;
        this.questionContent = questionContent;
        this.todayQuizAnswerOption = todayQuizAnswerOption;
    }

    public void initTodayQuizOption(TodayQuizOption todayQuizOption) {
        this.todayQuizOptions.add(todayQuizOption);
    }

    public boolean isValidOptionIndex(int submitOptionIndex) {
        return submitOptionIndex >= 0 && todayQuizOptions.size() > submitOptionIndex;
    }

    public boolean isInvalidOptionIndex(int submitOptionIndex) {
        return !isValidOptionIndex(submitOptionIndex);
    }

    public boolean isCorrect(int submittedOptionIndex) {
        validateIndex(submittedOptionIndex);

        TodayQuizOption todayQuizOption = todayQuizOptions.get(submittedOptionIndex);

        return todayQuizAnswerOption.matchesWordId(todayQuizOption.getWordId());
    }

    private void validateIndex(int submittedOptionIndex) {
        if (isInvalidOptionIndex(submittedOptionIndex)) {
            throw new InvalidSubmittedTodayQuizOptionIndexException("없는 보기를 선택했습니다.");
        }
    }

    public List<TodayQuizOption> getTodayQuizOptions() {
        return Collections.unmodifiableList(todayQuizOptions);
    }
}
