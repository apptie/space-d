package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.embed.CareerInfo;
import com.dnd.spaced.core.account.domain.embed.ProfileInfo;
import com.dnd.spaced.core.account.domain.embed.SocialInfo;
import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import com.dnd.spaced.core.account.domain.enums.Role;
import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "accounts")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean deleted = false;

    @Embedded
    SocialInfo socialInfo;

    @Embedded
    private ProfileInfo profileInfo;

    @Embedded
    private CareerInfo careerInfo;

    @Builder
    private Account(
            String nickname,
            String profileImage,
            Role role,
            RegistrationId registrationId,
            String socialIdentifier
    ) {
        this.profileInfo = ProfileInfo.of(nickname, profileImage);
        this.role = role;
        this.socialInfo = new SocialInfo(registrationId, socialIdentifier);
    }

    public void withdrawal() {
        this.deleted = true;
    }

    public void changeCareerInfo(
            String changedJobGroupName,
            String changedCompanyName,
            String changedExperienceName) {
        this.careerInfo = CareerInfo.builder()
                                    .jobGroupName(changedJobGroupName)
                                    .companyName(changedCompanyName)
                                    .experienceName(changedExperienceName)
                                    .build();
    }

    public void changeProfileInfo(String changedNickname, String changedProfileImage) {
        this.profileInfo = ProfileInfo.of(changedNickname, changedProfileImage);
    }

    public boolean isEqualTo(Long id) {
        return this.id.equals(id);
    }
}

