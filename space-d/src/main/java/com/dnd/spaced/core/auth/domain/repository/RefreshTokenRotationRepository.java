package com.dnd.spaced.core.auth.domain.repository;

import java.util.Optional;

public interface RefreshTokenRotationRepository {

    void save(String id, String refreshToken);

    Optional<String> findBy(String email);
}
