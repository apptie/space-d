package com.dnd.spaced.global.auth;

import org.springframework.stereotype.Component;

@Component
public class AuthStore {

    private final ThreadLocal<AccountId> threadLocalAuthenticationStore = new ThreadLocal<>();

    public void set(AccountId userInfo) {
        threadLocalAuthenticationStore.set(userInfo);
    }

    public AccountId get() {
        return threadLocalAuthenticationStore.get();
    }

    public void remove() {
        threadLocalAuthenticationStore.remove();
    }
}
