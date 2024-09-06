package com.dnd.spaced.core.admin.presentation;

import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.presentation.dto.request.SaveWordRequest;
import com.dnd.spaced.core.admin.presentation.dto.request.UpdateBlacklistTokenRequest;
import com.dnd.spaced.core.admin.presentation.dto.request.UpdateWordExampleRequest;
import com.dnd.spaced.core.auth.application.BlacklistTokenService;
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

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminWordService adminWordService;
    private final BlacklistTokenService blacklistTokenService;

    @PostMapping("/blacklist-token")
    public ResponseEntity<Void> registerBlacklistToken(@Valid @RequestBody UpdateBlacklistTokenRequest request) {
        blacklistTokenService.register(request.accountId());

        return ResponseEntityConst.CREATED_DEFAULT_CONTEXT_PATH;
    }

    @PostMapping("/words")
    public ResponseEntity<Void> saveWord(@Valid @RequestBody SaveWordRequest request) {
        SaveWordDto dto = request.to();

        Long wordId = adminWordService.saveWord(dto);

        return ResponseEntity.created(URI.create("/words/" + wordId))
                             .build();
    }

    @PatchMapping("/words/examples/{id}")
    public ResponseEntity<Void> updateWordExample(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWordExampleRequest request
    ) {
        adminWordService.updateWordExample(id, request.example());

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/words/examples/{id}")
    public ResponseEntity<Void> deleteWordExample(@PathVariable Long id) {
        adminWordService.deleteWordExample(id);

        return ResponseEntityConst.NO_CONTENT;
    }

    @DeleteMapping("/words/pronunciations/{id}")
    public ResponseEntity<Void> deletePronunciation(@PathVariable Long id) {
        adminWordService.deletePronunciation(id);

        return ResponseEntityConst.NO_CONTENT;
    }
}
