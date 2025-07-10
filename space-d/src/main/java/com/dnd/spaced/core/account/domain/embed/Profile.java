package com.dnd.spaced.core.account.domain.embed;

import com.dnd.spaced.core.account.domain.embed.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.embed.exception.InvalidProfileImageException;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private static final int NICKNAME_MIN_LENGTH = 5;
    private static final int NICKNAME_MAX_LENGTH = 10;
    private static final String NICKNAME_EXCEPTION_MESSAGE = String.format(
            "닉네임은 최소 %d글자 이상, 최대 %d글자 이하여야 합니다.",
            NICKNAME_MIN_LENGTH,
            NICKNAME_MAX_LENGTH
    );

    private String nickname;
    private String profileImageName;

    public static Profile of(String nickname, ProfileImageName profileImageName) {
        validateNickname(nickname);
        validateProfileImageName(profileImageName);

        return new Profile(nickname, profileImageName.getImageName());
    }

    private static void validateNickname(String nickname) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameException(NICKNAME_EXCEPTION_MESSAGE);
        }
    }

    private static void validateProfileImageName(ProfileImageName profileImageName) {
        if (isInvalidProfileImageName(profileImageName)) {
            throw new InvalidProfileImageException("프로필 이미지 정보는 null일 수 없습니다.");
        }
    }

    private static boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank()
                || nickname.length() < NICKNAME_MIN_LENGTH || nickname.length() > NICKNAME_MAX_LENGTH;
    }

    private static boolean isInvalidProfileImageName(ProfileImageName profileImageName) {
        return profileImageName == null;
    }

    private Profile(String nickname, String profileImageName) {
        this.nickname = nickname;
        this.profileImageName = profileImageName;
    }
}
