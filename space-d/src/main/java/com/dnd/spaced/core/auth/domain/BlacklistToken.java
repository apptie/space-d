package com.dnd.spaced.core.auth.domain;

import com.dnd.spaced.core.auth.domain.exception.InvalidBlacklistTokenContentException;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false, of = "email")
public class BlacklistToken {

    private final String email;
    private final LocalDateTime registeredAt;

    public BlacklistToken(String email, LocalDateTime registeredAt) {
        validateContent(email, registeredAt);

        this.email = email;
        this.registeredAt = registeredAt;
    }

    private void validateContent(String email, LocalDateTime registeredAt) {
        if (email == null || email.isBlank()) {
            throw new InvalidBlacklistTokenContentException("유효한 이메일이 아닙니다.");
        }

        if (registeredAt == null) {
            throw new InvalidBlacklistTokenContentException("유효한 등록 일자가 아닙니다.");
        }
    }

    public boolean isBlacklistToken(LocalDateTime targetIssuedAt) {
        return registeredAt.isAfter(targetIssuedAt);
    }
}
