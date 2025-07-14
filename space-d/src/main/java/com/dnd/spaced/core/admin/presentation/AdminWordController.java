package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminWordServiceFacade;
import com.dnd.spaced.core.admin.application.dto.request.CreateWordRequest;
import com.dnd.spaced.core.admin.application.dto.request.UpdateWordExampleRequest;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin/words")
@RequiredArgsConstructor
public class AdminWordController {

    private final AdminWordServiceFacade adminWordServiceFacade;

    @PostMapping
    public ResponseEntity<Void> createWord(@Valid @RequestBody CreateWordRequest request) {
        Long wordId = adminWordServiceFacade.createWord(request);
        URI location = UriComponentsBuilder.fromPath("/words/{wordId}")
                                           .buildAndExpand(wordId)
                                           .toUri();

        return ResponseEntity.created(location)
                             .build();
    }

    @PatchMapping("/examples/{wordExampleId}")
    public ResponseEntity<Void> updateWordExample(
            @PathVariable Long wordExampleId,
            @Valid @RequestBody UpdateWordExampleRequest request
    ) {
        adminWordServiceFacade.updateWordExample(wordExampleId, request.content());

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long wordId) {
        adminWordServiceFacade.deleteWord(wordId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/{wordId}/examples/{wordExampleId}")
    public ResponseEntity<Void> deleteWordExample(@PathVariable Long wordId, @PathVariable Long wordExampleId) {
        adminWordServiceFacade.deleteWordExample(wordId, wordExampleId);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/{wordId}/pronunciations/{pronunciationId}")
    public ResponseEntity<Void> deletePronunciation(@PathVariable Long wordId, @PathVariable Long pronunciationId) {
        adminWordServiceFacade.deletePronunciation(wordId, pronunciationId);

        return ResponseEntityConst.NO_CONTENT;
    }
}
