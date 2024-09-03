package com.dnd.spaced.core.account.domain.repository;

import com.dnd.spaced.core.account.domain.NicknameMetadata;
import java.util.Optional;

public interface NicknameMetadataRepository {

    NicknameMetadata save(NicknameMetadata nicknameMetadata);

    Optional<NicknameMetadata> findBy(String nickname);
}
