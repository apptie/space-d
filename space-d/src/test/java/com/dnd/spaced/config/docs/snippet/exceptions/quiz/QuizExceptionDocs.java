package com.dnd.spaced.config.docs.snippet.exceptions.quiz;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record QuizExceptionDocs(
        Map<String, ExceptionContent> createQuizException,
        Map<String, ExceptionContent> readQuizzesException,
        Map<String, ExceptionContent> findQuizByException,
        Map<String, ExceptionContent> gradeQuizException,
        Map<String, ExceptionContent> readGradedAnswerAllByQuizIdException,
        Map<String, ExceptionContent> readGradedAnswerAllException
) {
}
