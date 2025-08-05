package com.dnd.spaced.core.word.presentation;

import com.dnd.spaced.core.word.application.WordServiceFacade;
import com.dnd.spaced.core.word.application.dto.request.ReadAllWordRequest;
import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.global.resolver.word.WordPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

    private final WordServiceFacade wordServiceFacade;

    @GetMapping("/{wordId}")
    public ResponseEntity<WordResponse> readWord(@PathVariable Long wordId) {
        WordResponse response = wordServiceFacade.readWord(wordId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WordCollectionResponse> readWords(
            ReadAllWordRequest request,
            @WordPageable Pageable pageable
    ) {
        WordCollectionResponse response = wordServiceFacade.readWords(request, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WordCollectionResponse> searchWords(
            SearchWordRequest request,
            @WordPageable Pageable pageable
    ) {
        WordCollectionResponse response = wordServiceFacade.searchWord(request, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<PopularWordCollectionResponse> readPopularWords() {
        PopularWordCollectionResponse response = wordServiceFacade.readPopularWords();

        return ResponseEntity.ok(response);
    }
}
