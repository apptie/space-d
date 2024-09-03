package com.dnd.spaced.core.account.infrastructure.repository;

import com.dnd.spaced.core.account.domain.NicknameMetadata;
import org.springframework.data.repository.CrudRepository;

interface NicknameMetadataCrudRepository extends CrudRepository<NicknameMetadata, String> {
}
