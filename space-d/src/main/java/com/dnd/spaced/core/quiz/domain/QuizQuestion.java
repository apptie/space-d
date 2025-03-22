package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.embed.QuizAnswerOption;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizQuestionContentException;
import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizQuestionException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quiz_questions")
@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizQuestion {

    private static final int OPTION_SIZE = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    private QuizCategory quizCategory;

    @Embedded
    private QuizAnswerOption quizAnswerOption;

    private String question;

    private String passage;

    public static QuizQuestion of(
            QuizCategory quizCategory,
            String question,
            String questionContent,
            QuizAnswerOption quizAnswerOption,
            Quiz quiz
    ) {
        validateContent(question, questionContent);

        return new QuizQuestion(quizCategory, question, questionContent, quizAnswerOption, quiz);
    }

    private static void validateContent(String question, String questionContent) {
        if (question == null || question.isBlank()) {
            throw new InvalidQuizQuestionException("유효한 길이의 퀴즈 문제가 아닙니다.");
        }

        if (questionContent == null || questionContent.isBlank()) {
            throw new InvalidQuizQuestionContentException("유효한 길이의 퀴즈 문제 지문이 아닙니다.");
        }
    }

    private QuizQuestion(
            QuizCategory quizCategory,
            String question,
            String questionContent,
            QuizAnswerOption quizAnswerOption,
            Quiz quiz
    ) {
        this.quizCategory = quizCategory;
        this.question = question;
        this.passage = questionContent;
        this.quizAnswerOption = quizAnswerOption;
        this.quiz = quiz;
    }

    public boolean isValidOptionIndex(int submitOptionIndex) {
        return submitOptionIndex >= 0 && submitOptionIndex < OPTION_SIZE;
    }

    public boolean isInvalidOptionIndex(int optionIndex) {
        return !isValidOptionIndex(optionIndex);
    }

    public boolean isCorrect(Long submitWordId) {
        return quizAnswerOption.matchesWordId(submitWordId);
    }
}
