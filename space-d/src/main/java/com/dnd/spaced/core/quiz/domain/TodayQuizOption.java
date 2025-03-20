package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.exception.InvalidTodayQuizOptionContentException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "today_quiz_options")
@Entity
@Getter
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayQuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    private String content;

    private int optionOrder;

    private Long todayQuizId;

    public static TodayQuizOption of(Long wordId, String content, int index, TodayQuiz todayQuiz) {
        validateContent(content);

        return new TodayQuizOption(wordId, content, index, todayQuiz.getId());
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidTodayQuizOptionContentException("유효한 길이의 퀴즈 답 보기가 아닙니다.");
        }
    }

    private TodayQuizOption(Long wordId, String content, int optionOrder, Long todayQuizId) {
        this.wordId = wordId;
        this.content = content;
        this.optionOrder = optionOrder;
        this.todayQuizId = todayQuizId;
    }
}
