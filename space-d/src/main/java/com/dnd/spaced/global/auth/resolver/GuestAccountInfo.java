package com.dnd.spaced.global.auth.resolver;

import com.dnd.spaced.global.consts.AuthConst;

public record GuestAccountInfo(Long accountId) {

    public GuestAccountInfo() {
        this(AuthConst.GUEST_ACCOUNT_ID);
    }
}
