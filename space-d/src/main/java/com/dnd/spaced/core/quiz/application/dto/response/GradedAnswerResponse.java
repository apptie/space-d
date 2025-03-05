package com.dnd.spaced.core.quiz.application.dto.response;

public record GradedAnswerResponse(
        Long id,
        Long accountId,
        Long quizId,
        QuizQuestionResponse readQuizQuestion,
        String selectedQuizOptionContent,
        String answerQuizOptionContent,
        boolean isCorrect
) {

    public record QuizQuestionResponse(Long id, String quizCategory, String question, String questionContent) {
    }
}
