package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidJobGroupException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum JobGroup {

    DEVELOP("개발자"),
    DESIGN("디자이너"),
    ETC("기타");

    private static final String EXCEPTION_FORMAT = "잘못된 직군 이름 '%s'을(를) 입력했습니다.";

    private final String name;

    JobGroup(String name) {
        this.name = name;
    }

    public static JobGroup findBy(String name) {
        return Arrays.stream(JobGroup.values())
                     .filter(jobGroup -> jobGroup.name.equals(name))
                     .findAny()
                     .orElseThrow(() -> new InvalidJobGroupException(String.format(EXCEPTION_FORMAT, name)));
    }
}
