package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.exception.InvalidQuizOptionContentException;
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

@Entity
@Getter
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizOption extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    private String content;

    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_question_id")
    private QuizQuestion quizQuestion;

    public static QuizOption of(Long wordId, String content, int index, QuizQuestion quizQuestion) {
        validateContent(content);

        return new QuizOption(wordId, content, index, quizQuestion);
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidQuizOptionContentException("유효한 길이의 퀴즈 답 보기가 아닙니다.");
        }
    }

    private QuizOption(Long wordId, String content, int index, QuizQuestion quizQuestion) {
        this.wordId = wordId;
        this.content = content;
        this.index = index;
        this.quizQuestion = quizQuestion;

        quizQuestion.initQuizOption(this);
    }
}
