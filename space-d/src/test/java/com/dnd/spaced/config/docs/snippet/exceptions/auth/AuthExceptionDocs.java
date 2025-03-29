package com.dnd.spaced.config.docs.snippet.exceptions.auth;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record AuthExceptionDocs(
        Map<String, ExceptionContent> initAccountCareerInfoException,
        Map<String, ExceptionContent> refreshTokenException,
        Map<String, ExceptionContent> registerBlacklistTokenException
) {
}
