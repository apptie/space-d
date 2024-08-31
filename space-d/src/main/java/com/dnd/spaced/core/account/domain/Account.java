package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidEmailException;
import com.dnd.spaced.core.account.domain.exception.InvalidNicknameException;
import com.dnd.spaced.core.account.domain.exception.InvalidProfileImageException;
import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Table(name = "accounts")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends CreateTimeEntity implements Persistable<String> {

    private static final int NICKNAME_MIN_LENGTH = 5;
    private static final int NICKNAME_MAX_LENGTH = 10;
    private static final String NICKNAME_EXCEPTION_MESSAGE = String.format(
            "닉네임은 최소 %d글자 이상, 최대 %d글자 이하여야 합니다.",
            NICKNAME_MIN_LENGTH,
            NICKNAME_MAX_LENGTH
    );

    @Id
    private String id;

    private String nickname;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    CareerInfo careerInfo;

    @Builder
    private Account(String id, String nickname, String profileImage, String roleName) {
        validateContent(id, nickname, profileImage);

        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = Role.findBy(roleName);
    }

    private void validateContent(String email, String nickname, String profileImage) {
        if (isInvalidEmail(email)) {
            throw new InvalidEmailException("이메일은 null이나 비어 있을 수 없습니다.");
        }

        validateProfileInfo(nickname, profileImage);
    }

    public void changeCareerInfo(String jobGroupName, String companyName, String experienceName) {
        this.careerInfo = CareerInfo.builder()
                                    .jobGroupName(jobGroupName)
                                    .companyName(companyName)
                                    .experienceName(experienceName)
                                    .build();
    }

    public void changeProfileInfo(String changedNickname, String changedProfileImage) {
        validateProfileInfo(changedNickname, changedProfileImage);

        this.nickname = changedNickname;
        this.profileImage = changedProfileImage;
    }

    private void validateProfileInfo(String nickname, String profileImage) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameException(NICKNAME_EXCEPTION_MESSAGE);
        }
        if (isInvalidProfileImage(profileImage)) {
            throw new InvalidProfileImageException("프로필 이미지 정보는 null이거나 비어 있을 수 없습니다.");
        }
    }

    private boolean isInvalidEmail(String email) {
        return email == null || email.isBlank();
    }

    private boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank()
                || nickname.length() < NICKNAME_MIN_LENGTH || nickname.length() > NICKNAME_MAX_LENGTH;
    }

    private boolean isInvalidProfileImage(String profileImage) {
        return profileImage == null || profileImage.isBlank();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
