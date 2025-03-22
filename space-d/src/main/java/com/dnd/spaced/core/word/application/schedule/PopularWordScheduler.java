package com.dnd.spaced.core.word.application.schedule;

import com.dnd.spaced.core.word.domain.repository.PopularWordRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.core.word.domain.repository.WordViewCountStatisticsRepository;
import com.dnd.spaced.core.word.domain.dto.PopularWord;
import com.dnd.spaced.core.word.domain.repository.dto.WordViewCountStatisticsDto;
import com.dnd.spaced.core.word.domain.dto.ViewCountStatisticsRank;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PopularWordScheduler {

    private final Clock clock;
    private final WordRepository wordRepository;
    private final PopularWordRepository popularWordRepository;
    private final WordViewCountStatisticsRepository wordViewCountStatisticsRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void schedule() {
        LocalDateTime today = LocalDateTime.now(clock);
        LocalDateTime yesterday = today.minusDays(1L);
        LocalDateTime beforeYesterday = yesterday.minusDays(1L);

        updatePopularWordViewCount(yesterday);
        updatePopularWord(today);
        clearViewCountMetadata(yesterday, beforeYesterday);
    }

    private void updatePopularWord(LocalDateTime today) {
        List<ViewCountStatisticsRank> ranking = wordViewCountStatisticsRepository.findAllBy(today);
        Long[] ids = ranking.stream()
                              .map(ViewCountStatisticsRank::wordId)
                              .toArray(Long[]::new);
        List<String> names = wordRepository.findNameAllBy(ids);
        List<PopularWord> popularWords = calculatePopularWordInfo(ranking, names);

        popularWordRepository.saveAll(popularWords, today);
    }

    private void updatePopularWordViewCount(LocalDateTime yesterday) {
        List<Long> ids = popularWordRepository.findAllBy(yesterday)
                                              .stream()
                                              .map(PopularWord::wordId)
                                              .toList();
        List<WordViewCountStatisticsDto> dtos = wordViewCountStatisticsRepository.findAllBy(ids, yesterday);

        wordRepository.updateViewCount(dtos);
    }

    private List<PopularWord> calculatePopularWordInfo(List<ViewCountStatisticsRank> ranking, List<String> names) {
        List<PopularWord> popularWords = new ArrayList<>();

        for (int i = 0; i < ranking.size(); i++) {
            ViewCountStatisticsRank targetRankDto = ranking.get(i);
            String targetName = names.get(i);

            popularWords.add(new PopularWord(targetRankDto.rank(), targetRankDto.wordId(), targetName));
        }

        return popularWords;
    }

    private void clearViewCountMetadata(LocalDateTime yesterday, LocalDateTime beforeYesterday) {
        popularWordRepository.deleteAll(yesterday);
        wordViewCountStatisticsRepository.deleteAll(beforeYesterday);
    }
}
