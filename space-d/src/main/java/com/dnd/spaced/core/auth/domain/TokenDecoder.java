package com.dnd.spaced.core.auth.domain;

import com.dnd.spaced.core.auth.domain.enums.TokenType;
import java.util.Optional;

public interface TokenDecoder {

    Optional<PrivateClaims> decode(TokenType tokenType, String token);
}
