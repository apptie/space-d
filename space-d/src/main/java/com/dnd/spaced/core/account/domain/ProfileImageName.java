package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageNameException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProfileImageName {
    MERCURY("수성", "mercury.png"),
    VENUS("금성", "venus.png"),
    EARTH("지구", "earth.png"),
    MARS("화성", "mars.png"),
    JUPITER("목성", "jupiter.png"),
    SATURN("토성", "saturn.png"),
    URANUS("천왕성", "uranus.png"),
    NEPTUNE("해왕성", "neptune.png");

    private static final String EXCEPTION_MESSAGE = "잘못된 프로필 이미지 이름 '%s'를 입력했습니다.";

    private final String korean;
    private final String imageName;

    ProfileImageName(String korean, String imageName) {
        this.korean = korean;
        this.imageName = imageName;
    }

    public static ProfileImageName findRandom() {
        return Arrays.stream(ProfileImageName.values())
                     .findAny()
                     .orElse(EARTH);
    }

    public static ProfileImageName findBy(String korean) {
        return Arrays.stream(ProfileImageName.values())
                     .filter(profileImageName -> profileImageName.korean.equals(korean))
                     .findAny()
                     .orElseThrow(() -> new InvalidProfileImageNameException(String.format(EXCEPTION_MESSAGE, korean)));
    }
}
