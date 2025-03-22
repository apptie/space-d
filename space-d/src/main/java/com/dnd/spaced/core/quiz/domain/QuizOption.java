package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizOptionContentException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quiz_options")
@Entity
@Getter
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    private String content;

    private int optionOrder;

    private Long quizQuestionId;

    public static QuizOption of(Long wordId, String content, int optionOrder, Long quizQuestionId) {
        validateContent(content);

        return new QuizOption(wordId, content, optionOrder, quizQuestionId);
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidQuizOptionContentException("유효한 길이의 퀴즈 답 보기가 아닙니다.");
        }
    }

    private QuizOption(Long wordId, String content, int optionOrder, Long quizQuestionId) {
        this.wordId = wordId;
        this.content = content;
        this.optionOrder = optionOrder;
        this.quizQuestionId = quizQuestionId;
    }
}
