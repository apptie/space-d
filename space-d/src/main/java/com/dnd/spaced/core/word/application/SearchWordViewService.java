package com.dnd.spaced.core.word.application;

import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.WordViewRepository;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SearchWordViewService {

    private static final Category NO_CATEGORY_FILTER = null;
    private static final Category NO_LAST_CATEGORY_FILTER = null;

    private final WordViewRepository wordViewRepository;

    public List<WordView> searchWord(SearchWordRequest request, Pageable pageable) {
        WordSearchCondition wordSearchCondition = buildWordSearchCondition(
                request);
        WordSearchPageRequest wordSearchPageRequest = buildWordSearchPageRequest(
                request, pageable);
        return wordViewRepository.search(wordSearchCondition, wordSearchPageRequest);
    }

    private WordSearchCondition buildWordSearchCondition(SearchWordRequest request) {
        Category category = Category.findBy(request.categoryName())
                                    .orElse(NO_CATEGORY_FILTER);

        return new WordSearchCondition(
                request.name(),
                category,
                request.pronunciation()
        );
    }

    private WordSearchPageRequest buildWordSearchPageRequest(SearchWordRequest request, Pageable pageable) {
        Category lastCategory = Category.findBy(request.lastCategoryName())
                                        .orElse(NO_LAST_CATEGORY_FILTER);

        return new WordSearchPageRequest(
                pageable,
                request.lastWordName(),
                lastCategory
        );
    }
}
