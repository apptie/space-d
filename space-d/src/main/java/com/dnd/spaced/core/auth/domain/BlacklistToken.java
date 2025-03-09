package com.dnd.spaced.core.auth.domain;

import com.dnd.spaced.core.auth.domain.exception.InvalidBlacklistTokenContentException;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false, of = "accountId")
public class BlacklistToken {

    private final Long accountId;
    private final LocalDateTime registeredAt;

    public static BlacklistToken of(Long accountId, LocalDateTime registeredAt) {
        validateContent(accountId, registeredAt);

        return new BlacklistToken(accountId, registeredAt);
    }

    private static void validateContent(Long accountId, LocalDateTime registeredAt) {
        if (accountId == null) {
            throw new InvalidBlacklistTokenContentException("유효한 ID가 아닙니다.");
        }

        if (registeredAt == null) {
            throw new InvalidBlacklistTokenContentException("유효한 등록 일자가 아닙니다.");
        }
    }

    private BlacklistToken(Long accountId, LocalDateTime registeredAt) {
        this.accountId = accountId;
        this.registeredAt = registeredAt;
    }

    public boolean isBlacklistToken(LocalDateTime targetIssuedAt) {
        return registeredAt.isAfter(targetIssuedAt);
    }
}
