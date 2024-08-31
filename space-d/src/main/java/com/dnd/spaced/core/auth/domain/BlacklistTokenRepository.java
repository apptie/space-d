package com.dnd.spaced.core.auth.domain;

import java.util.Optional;

public interface BlacklistTokenRepository {

    Optional<BlacklistToken> findBy(String id);

    void save(BlacklistToken blacklistToken);
}
