package com.dnd.spaced.global.auth.resolver;

public record GuestAccountInfo(Long accountId) {

    private static final Long GUEST_ACCOUNT_ID = -1L;

    public GuestAccountInfo() {
        this(GUEST_ACCOUNT_ID);
    }
}
