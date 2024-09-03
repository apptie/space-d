package com.dnd.spaced.core.account.infrastructure.repository;

import static com.dnd.spaced.core.account.domain.QNicknameMetadata.nicknameMetadata;

import com.dnd.spaced.core.account.domain.NicknameMetadata;
import com.dnd.spaced.core.account.domain.repository.NicknameMetadataRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NicknameMetadataQuerydslRepository implements NicknameMetadataRepository {

    private final JPAQueryFactory queryFactory;
    private final NicknameMetadataCrudRepository nicknameMetadataCrudRepository;


    @Override
    public NicknameMetadata save(NicknameMetadata nicknameMetadata) {
        return nicknameMetadataCrudRepository.save(nicknameMetadata);
    }

    @Override
    public Optional<NicknameMetadata> findBy(String nickname) {
        NicknameMetadata result = queryFactory.selectFrom(nicknameMetadata)
                                              .where(nicknameMetadata.nickname.eq(nickname))
                                              .setLockMode(LockModeType.PESSIMISTIC_READ)
                                              .fetchOne();

        return Optional.ofNullable(result);
    }
}
