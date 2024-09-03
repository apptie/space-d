package com.dnd.spaced.global.exception.code;

public enum AuthErrorCode implements ErrorCode {

    INVALID_TOKEN,
    INVALID_BLACKLIST_TOKEN_CONTENT,
    FORBIDDEN_INIT_CAREER_INFO,
    REFRESH_TOKEN_NOT_FOUND,
    EXPIRED_TOKEN,
    BLOCKED_TOKEN,
    ROTATION_REFRESH_TOKEN_MISMATCH
}
