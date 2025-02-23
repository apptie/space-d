package com.dnd.spaced.core.auth.domain.repository;

import java.util.Optional;

public interface RefreshTokenRotationRepository {

    void save(Long accountId, String refreshToken);

    Optional<String> findBy(Long accountId);
}
