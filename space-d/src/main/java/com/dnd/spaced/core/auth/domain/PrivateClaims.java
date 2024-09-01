package com.dnd.spaced.core.auth.domain;

import java.time.LocalDateTime;

public record PrivateClaims(String id, String roleName, LocalDateTime issuedAt) {
}
