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
}
