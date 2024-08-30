package com.dnd.spaced.core.auth.domain;

import java.util.Optional;

public interface RefreshTokenRotationRepository {

    void save(String email, String refreshToken);

    Optional<String> findBy(String email);
}
