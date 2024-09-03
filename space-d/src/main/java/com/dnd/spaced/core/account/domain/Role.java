package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidRoleNameException;
import java.util.Arrays;

public enum Role {

    ROLE_ADMIN, ROLE_USER;

    private static final String EXCEPTION_FORMAT = "잘못된 role name '%s'을(를) 입력했습니다.";

    public static Role findBy(String roleName) {
        return Arrays.stream(Role.values())
                     .filter(role -> role.name().equalsIgnoreCase(roleName))
                     .findAny()
                     .orElseThrow(() -> new InvalidRoleNameException(String.format(EXCEPTION_FORMAT, roleName)));
    }
}
