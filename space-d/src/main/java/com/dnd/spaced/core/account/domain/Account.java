package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidIdException;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.domain.Persistable;

@Table(name = "accounts")
@Getter
@Entity
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends CreateTimeEntity implements Persistable<String> {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Role role;

    boolean deleted = false;

    @Embedded
    private ProfileInfo profileInfo;

    @Embedded
    CareerInfo careerInfo;

    @Builder
    private Account(String id, String nickname, String profileImage, String roleName) {
        validateContent(id);

        this.id = id;
        this.profileInfo = new ProfileInfo(nickname, profileImage);
        this.role = Role.findBy(roleName);
    }

    private void validateContent(String id) {
        if (isInvalidId(id)) {
            throw new InvalidIdException("ID는 null이나 비어 있을 수 없습니다.");
        }
    }

    public void changeCareerInfo(String jobGroupName, String companyName, String experienceName) {
        this.careerInfo = CareerInfo.builder()
                                    .jobGroupName(jobGroupName)
                                    .companyName(companyName)
                                    .experienceName(experienceName)
                                    .build();
    }

    public void changeProfileInfo(String changedNickname, String changedProfileImage) {
        this.profileInfo.changeProfileInfo(changedNickname, changedProfileImage);
    }

    private boolean isInvalidId(String id) {
        return id == null || id.isBlank();
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
