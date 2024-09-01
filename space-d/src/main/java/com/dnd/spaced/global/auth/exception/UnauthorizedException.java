package com.dnd.spaced.global.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException() {
        super("인증이 필요한 기능입니다.");
    }
}
