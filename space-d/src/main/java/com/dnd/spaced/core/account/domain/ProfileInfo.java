package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileInfo {

    private static final int NICKNAME_MIN_LENGTH = 5;
    private static final int NICKNAME_MAX_LENGTH = 10;
    private static final String NICKNAME_EXCEPTION_MESSAGE = String.format(
            "닉네임은 최소 %d글자 이상, 최대 %d글자 이하여야 합니다.",
            NICKNAME_MIN_LENGTH,
            NICKNAME_MAX_LENGTH
    );

    private String nickname;
    private String profileImage;

    public ProfileInfo(String nickname, String profileImage) {
        validateContent(nickname, profileImage);

        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void changeProfileInfo(String changedNickname, String changedProfileImage) {
        validateContent(changedNickname, changedProfileImage);

        this.nickname = changedNickname;
        this.profileImage = changedProfileImage;
    }

    private void validateContent(String nickname, String profileImage) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameException(NICKNAME_EXCEPTION_MESSAGE);
        }
        if (isInvalidProfileImage(profileImage)) {
            throw new InvalidProfileImageException("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
        }
    }

    private boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank()
                || nickname.length() < NICKNAME_MIN_LENGTH || nickname.length() > NICKNAME_MAX_LENGTH;
    }

    private boolean isInvalidProfileImage(String profileImage) {
        return profileImage == null || profileImage.isBlank();
    }
}
