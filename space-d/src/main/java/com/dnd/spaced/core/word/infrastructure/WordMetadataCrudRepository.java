package com.dnd.spaced.core.word.infrastructure;

import com.dnd.spaced.core.word.domain.WordMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

interface WordMetadataCrudRepository extends JpaRepository<WordMetadata, Long> {
}
