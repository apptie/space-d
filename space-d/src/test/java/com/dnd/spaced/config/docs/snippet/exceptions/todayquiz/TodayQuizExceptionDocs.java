package com.dnd.spaced.config.docs.snippet.exceptions.todayquiz;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record TodayQuizExceptionDocs(
        Map<String, ExceptionContent> readLatestTodayQuizException,
        Map<String, ExceptionContent> readTodayQuizException,
        Map<String, ExceptionContent> gradeTodayQuizException,
        Map<String, ExceptionContent> readTargetTodayQuizGradedAnswersException,
        Map<String, ExceptionContent> readTodayQuizGradedAnswersException
) {
}
