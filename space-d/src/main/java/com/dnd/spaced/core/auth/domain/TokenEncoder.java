package com.dnd.spaced.core.auth.domain;

import java.time.LocalDateTime;

public interface TokenEncoder {

    String encode(LocalDateTime targetTime, TokenType tokenType, String accountId, String roleName);
}
