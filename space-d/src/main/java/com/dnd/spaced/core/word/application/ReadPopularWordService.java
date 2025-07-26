package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.domain.dto.PopularWord;
import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadPopularWordService {

    private final Clock clock;
    private final PopularWordRepository popularWordRepository;

    public List<PopularWord> readPopularWords() {
        return popularWordRepository.findAllBy(LocalDateTime.now(clock));
    }
}
