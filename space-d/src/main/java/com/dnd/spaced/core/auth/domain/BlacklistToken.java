package com.dnd.spaced.core.auth.domain;

import com.dnd.spaced.core.auth.domain.exception.InvalidBlacklistTokenContentException;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false, of = "accountId")
public class BlacklistToken {

    private final String accountId;
    private final LocalDateTime registeredAt;

    public BlacklistToken(String accountId, LocalDateTime registeredAt) {
        validateContent(accountId, registeredAt);

        this.accountId = accountId;
        this.registeredAt = registeredAt;
    }

    private void validateContent(String id, LocalDateTime registeredAt) {
        if (id == null || id.isBlank()) {
            throw new InvalidBlacklistTokenContentException("유효한 ID가 아닙니다.");
        }

        if (registeredAt == null) {
            throw new InvalidBlacklistTokenContentException("유효한 등록 일자가 아닙니다.");
        }
    }

    public boolean isBlacklistToken(LocalDateTime targetIssuedAt) {
        return registeredAt.isAfter(targetIssuedAt);
    }
}
