package com.dnd.spaced.core.auth.domain;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import java.time.LocalDateTime;

public interface TokenEncoder {

    String encode(LocalDateTime targetTime, TokenType tokenType, Long accountId, String roleName);
}
