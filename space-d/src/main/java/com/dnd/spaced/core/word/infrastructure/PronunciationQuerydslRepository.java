package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PronunciationQuerydslRepository implements PronunciationRepository {

    private final PronunciationCrudRepository pronunciationCrudRepository;

    @Override
    public void deleteBy(Long id) {
        pronunciationCrudRepository.deleteById(id);
    }
}
