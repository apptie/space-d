package com.dnd.spaced.core.quiz.domain;

import com.dnd.spaced.core.quiz.domain.exception.InvalidSubmittedAnswersCountException;
import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quizzes")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseTimeEntity {

    private static final int DEFAULT_QUESTION_SIZE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @OneToMany(mappedBy = "quiz")
    @Getter(AccessLevel.NONE)
    private List<QuizQuestion> quizQuestions = new ArrayList<>();

    private boolean solved = false;

    public Quiz(Long accountId) {
        this.accountId = accountId;
    }

    void initQuestion(QuizQuestion quizQuestion) {
        this.quizQuestions.add(quizQuestion);
    }

    public void solve() {
        this.solved = true;
    }

    public List<GradedAnswer> grade(Long accountId, List<SubmitAnswer> submitAnswers) {
        validateAnswers(submitAnswers);

        return gradeQuestions(accountId, submitAnswers);
    }

    private void validateAnswers(List<SubmitAnswer> submitAnswers) {
        if (submitAnswers.size() != DEFAULT_QUESTION_SIZE) {
            throw new InvalidSubmittedAnswersCountException("문제 개수와 제출한 정답 개수가 다릅니다.");
        }
    }

    private List<GradedAnswer> gradeQuestions(Long accountId, List<SubmitAnswer> submitAnswers) {
        List<GradedAnswer> gradedAnswers = new ArrayList<>();

        for (int i = 0; i < submitAnswers.size(); i++) {
            SubmitAnswer submitAnswer = submitAnswers.get(i);
            GradedAnswer gradedAnswer = GradedAnswer.of(
                    accountId,
                    this.id,
                    quizQuestions.get(i),
                    submitAnswer.wordId,
                    submitAnswer.content
            );

            gradedAnswers.add(gradedAnswer);
        }

        return gradedAnswers;
    }

    public List<QuizQuestion> getQuizQuestions() {
        return Collections.unmodifiableList(quizQuestions);
    }

    public record SubmitAnswer(Long wordId, String content) {
    }
}
