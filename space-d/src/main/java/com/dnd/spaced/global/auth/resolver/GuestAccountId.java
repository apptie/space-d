package com.dnd.spaced.global.auth.resolver;

import com.dnd.spaced.global.consts.AuthConst;

public record GuestAccountId(Long id) {

    public GuestAccountId() {
        this(AuthConst.GUEST_ACCOUNT_ID);
    }
}
