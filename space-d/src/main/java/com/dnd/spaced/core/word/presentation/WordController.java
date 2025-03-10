package com.dnd.spaced.core.word.presentation;

import com.dnd.spaced.core.word.application.WordService;
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

    private final WordService wordService;

    @GetMapping("/{wordId}")
    public ResponseEntity<WordResponse> read(@PathVariable Long wordId) {
        WordResponse response = wordService.read(wordId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WordCollectionResponse> readAllBy(
            ReadAllWordRequest request,
            @WordPageable Pageable pageable
    ) {
        WordCollectionResponse response = wordService.readAllBy(request, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WordCollectionResponse> search(
            SearchWordRequest request,
            @WordPageable Pageable pageable
    ) {
        WordCollectionResponse response = wordService.search(request, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<PopularWordCollectionResponse> readPopularWordsAll() {
        PopularWordCollectionResponse response = wordService.readPopularWordsAll();

        return ResponseEntity.ok(response);
    }
}
