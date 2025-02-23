package com.dnd.spaced.core.auth.domain.repository;

import com.dnd.spaced.core.auth.domain.BlacklistToken;
import java.util.Optional;

public interface BlacklistTokenRepository {

    Optional<BlacklistToken> findBy(Long accountId);

    void save(BlacklistToken blacklistToken);
}
