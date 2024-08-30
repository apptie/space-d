package com.dnd.spaced.core.account.domain;

import com.dnd.spaced.core.account.domain.exception.InvalidNicknameMetadataException;
import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameMetadata extends CreateTimeEntity implements Persistable<String> {

    private static final long START_COUNT_VALUE = 1L;
    private static final int NICKNAME_MIN_LENGTH = 5;
    private static final int NICKNAME_MAX_LENGTH = 6;
    private static final String NICKNAME_EXCEPTION_MESSAGE = String.format(
            "닉네임은 최소 %d글자 이상, 최대 %d글자 이하여야 합니다.",
            NICKNAME_MIN_LENGTH,
            NICKNAME_MAX_LENGTH
    );

    @Id
    private String nickname;

    private long count = START_COUNT_VALUE;

    public void addCount() {
        this.count++;
    }

    public NicknameMetadata(String nickname) {
        validateContent(nickname);

        this.nickname = nickname;
    }

    private void validateContent(String nickname) {
        if (isInvalidNickname(nickname)) {
            throw new InvalidNicknameMetadataException(NICKNAME_EXCEPTION_MESSAGE);
        }
    }

    private boolean isInvalidNickname(String nickname) {
        return nickname == null || nickname.isBlank()
                || nickname.length() < NICKNAME_MIN_LENGTH || nickname.length() > NICKNAME_MAX_LENGTH;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }

    @Override
    public String getId() {
        return nickname;
    }
}
