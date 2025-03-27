package com.dnd.spaced.config.docs.snippet.exceptions.account;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record AccountExceptionDocs(
        Map<String, ExceptionContent> authProfileException,
        Map<String, ExceptionContent> withdrawalException,
        Map<String, ExceptionContent> changeCareerInfoException,
        Map<String, ExceptionContent> changeProfileInfoException,
        Map<String, ExceptionContent> findAccountInfoException
) {
}
