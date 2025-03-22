package com.dnd.spaced.core.quiz.domain.embed;

import com.dnd.spaced.core.quiz.domain.TodayQuizOption;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizExampleContentException;
import com.dnd.spaced.core.quiz.domain.embed.exception.InvalidTodayQuizQuestionException;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuizQuestion {

    @Enumerated(EnumType.STRING)
    private QuizCategory quizCategory;

    private String question;

    private String passage;

    @Embedded
    private TodayQuizAnswerOption todayQuizAnswerOption;

    @OneToMany(mappedBy = "todayQuiz")
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
            String passage,
            TodayQuizAnswerOption todayQuizAnswerOption
    ) {
        this.quizCategory = quizCategory;
        this.question = question;
        this.passage = passage;
        this.todayQuizAnswerOption = todayQuizAnswerOption;
    }

    public boolean isCorrect(Long wordId) {
        return todayQuizAnswerOption.matchesWordId(wordId);
    }
}
