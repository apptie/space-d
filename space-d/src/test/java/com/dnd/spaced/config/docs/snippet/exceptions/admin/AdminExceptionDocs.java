package com.dnd.spaced.config.docs.snippet.exceptions.admin;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record AdminExceptionDocs(
        Map<String, ExceptionContent> registerBlacklistTokenException,
        Map<String, ExceptionContent> createWordException,
        Map<String, ExceptionContent> updateWordExampleException,
        Map<String, ExceptionContent> deleteWordExampleException,
        Map<String, ExceptionContent> deletePronunciationException,
        Map<String, ExceptionContent> processReportException,
        Map<String, ExceptionContent> createTodayQuizException,
        Map<String, ExceptionContent> deleteWordException
) {
}
