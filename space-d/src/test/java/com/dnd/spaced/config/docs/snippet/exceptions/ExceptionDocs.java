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

    private Map<String, ExceptionContent> authProfileException;
    private Map<String, ExceptionContent> refreshTokenException;
    private Map<String, ExceptionContent> registerBlacklistTokenException;
    private Map<String, ExceptionContent> withdrawalException;
    private Map<String, ExceptionContent> changeCareerInfoException;
    private Map<String, ExceptionContent> changeProfileInfoException;
    private Map<String, ExceptionContent> findAccountInfoException;
    private Map<String, ExceptionContent> saveWordException;
    private Map<String, ExceptionContent> updateWordExampleException;
    private Map<String, ExceptionContent> deleteWordExampleException;
    private Map<String, ExceptionContent> deletePronunciationException;
    private Map<String, ExceptionContent> readWordException;
    private Map<String, ExceptionContent> saveCommentException;
    private Map<String, ExceptionContent> deleteCommentException;
    private Map<String, ExceptionContent> updateCommentException;
    private Map<String, ExceptionContent> processLikeException;
    private Map<String, ExceptionContent> createQuizException;
    private Map<String, ExceptionContent> gradeQuizException;
    private Map<String, ExceptionContent> findGradedAnswersAllByException;
    private Map<String, ExceptionContent> findGradedAnswersAllByQuizException;
    private Map<String, ExceptionContent> findQuizByException;
    private Map<String, ExceptionContent> findLatestTodayQuizException;
    private Map<String, ExceptionContent> findTodayQuizByException;
    private Map<String, ExceptionContent> gradeTodayQuizException;
    private Map<String, ExceptionContent> findTodayQuizGradedAnswerByException;
    private Map<String, ExceptionContent> findTodayQuizGradedAnswersAllByException;
    private Map<String, ExceptionContent> createTodayQuizException;
    private Map<String, ExceptionContent> readLocalImageException;
    private Map<String, ExceptionContent> reportException;
    private Map<String, ExceptionContent> processReportException;
    private Map<String, ExceptionContent> createBookmarkException;
    private Map<String, ExceptionContent> deleteBookmarkException;
    private Map<String, ExceptionContent> findAllBookmarkException;
    private Map<String, ExceptionContent> findSkillException;
}
