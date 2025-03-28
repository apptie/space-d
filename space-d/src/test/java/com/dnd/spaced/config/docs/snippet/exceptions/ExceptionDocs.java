package com.dnd.spaced.config.docs.snippet.exceptions;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDocs {

    private Map<String, ExceptionContent> findLatestTodayQuizException;
    private Map<String, ExceptionContent> findTodayQuizByException;
    private Map<String, ExceptionContent> gradeTodayQuizException;
    private Map<String, ExceptionContent> findTodayQuizGradedAnswerByException;
    private Map<String, ExceptionContent> findTodayQuizGradedAnswersAllByException;
    private Map<String, ExceptionContent> readLocalImageException;
    private Map<String, ExceptionContent> createBookmarkException;
    private Map<String, ExceptionContent> deleteBookmarkException;
    private Map<String, ExceptionContent> findAllBookmarkException;
    private Map<String, ExceptionContent> findSkillException;
    private Map<String, ExceptionContent> readQuizzesException;
}
