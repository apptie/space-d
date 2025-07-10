package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.embed.Career;
import com.dnd.spaced.core.account.domain.embed.Profile;
import com.dnd.spaced.core.account.domain.embed.Social;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
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
    private Social social;

    @Embedded
    private Profile profile;

    @Embedded
    private Career career;

    @Builder
    private Account(
            String nickname,
            ProfileImageName profileImageName,
            Role role,
            RegistrationId registrationId,
            String socialIdentifier
    ) {
        this.profile = Profile.of(nickname, profileImageName);
        this.role = role;
        this.social = new Social(registrationId, socialIdentifier);
    }

    public void withdrawal() {
        this.deleted = true;
    }

    public void changeCareer(
            String changedJobGroupName,
            String changedCompanyName,
            String changedExperienceName) {
        this.career = Career.builder()
                            .jobGroupName(changedJobGroupName)
                            .companyName(changedCompanyName)
                            .experienceName(changedExperienceName)
                            .build();
    }

    public void changeProfileInfo(String changedNickname, ProfileImageName changedProfileImageName) {
        this.profile = Profile.of(changedNickname, changedProfileImageName);
    }

    public boolean isEqualTo(Long id) {
        return this.id.equals(id);
    }
}

