package com.dnd.spaced.core.auth.domain.repository;

import java.util.Optional;

public interface RefreshTokenRotationRepository {

    void save(String accountId, String refreshToken);

    Optional<String> findBy(String accountId);
}
