package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationTypeNameException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PronunciationType {
    KOREAN("한글 발음"), US("미국 발음"), UK("영국 발음");

    private final String name;

    private static final String EXCEPTION_MESSAGE = "잘못된 발음 타입 '%s'를 입력했습니다.";

    PronunciationType(String name) {
        this.name = name;
    }

    public static PronunciationType findBy(String name) {
        return Arrays.stream(PronunciationType.values())
                     .filter(pronunciationType -> pronunciationType.name.equals(name))
                     .findAny()
                     .orElseThrow(() -> new InvalidPronunciationTypeNameException(String.format(EXCEPTION_MESSAGE, name)));
    }
}
