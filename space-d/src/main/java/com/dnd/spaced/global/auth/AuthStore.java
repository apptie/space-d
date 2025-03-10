package com.dnd.spaced.global.auth;

import org.springframework.stereotype.Component;

@Component
public class AuthStore {

    private final ThreadLocal<AccountInfo> threadLocalAuthenticationStore = new ThreadLocal<>();

    public void set(AccountInfo userInfo) {
        threadLocalAuthenticationStore.set(userInfo);
    }

    public AccountInfo get() {
        return threadLocalAuthenticationStore.get();
    }

    public void remove() {
        threadLocalAuthenticationStore.remove();
    }
}
