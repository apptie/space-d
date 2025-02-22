package com.dnd.spaced.core.account.domain.enums;

import com.dnd.spaced.core.account.domain.enums.exception.InvalidRegistrationIdException;
import java.util.Arrays;

public enum RegistrationId {

    KAKAO("kakao");

    private static final String EXCEPTION_FORMAT = "잘못된 registration id '%s'을(를) 입력했습니다.";

    private final String name;

    RegistrationId(String name) {
        this.name = name;
    }

    public static RegistrationId findBy(String name) {
        return Arrays.stream(RegistrationId.values())
                     .filter(id -> id.name.equalsIgnoreCase(name))
                     .findAny()
                     .orElseThrow(() -> new InvalidRegistrationIdException(String.format(EXCEPTION_FORMAT, name)));
    }

    public static boolean supports(String name) {
        return Arrays.stream(RegistrationId.values())
                     .anyMatch(id -> id.name.equals(name));
    }
}
