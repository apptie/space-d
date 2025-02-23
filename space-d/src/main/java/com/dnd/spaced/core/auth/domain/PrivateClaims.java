package com.dnd.spaced.core.auth.domain;

import java.time.LocalDateTime;

public record PrivateClaims(Long accountId, String roleName, LocalDateTime issuedAt) {
}
