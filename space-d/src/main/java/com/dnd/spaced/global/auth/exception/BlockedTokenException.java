package com.dnd.spaced.global.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class BlockedTokenException extends AuthenticationException {

    public BlockedTokenException() {
        super("세션이 만료되었습니다. 다시 로그인 해 주세요.");
    }
}
