package com.dnd.spaced.global.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidResponseWriteException extends AuthenticationException {

    public InvalidResponseWriteException() {
        super("응답 메시지를 추가하는 과정에 문제가 발생했습니다.");
    }
}
