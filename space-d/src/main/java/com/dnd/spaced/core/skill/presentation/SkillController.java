package com.dnd.spaced.core.skill.presentation;

import com.dnd.spaced.core.skill.application.SkillService;
import com.dnd.spaced.core.skill.application.dto.response.SkillResponse;
import com.dnd.spaced.global.auth.resolver.CurrentAccountInfo;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<SkillResponse> readSkill(@CurrentAccountInfo AuthAccountInfo accountInfo) {
        SkillResponse response = skillService.readSkill(accountInfo.accountId());

        return ResponseEntity.ok(response);
    }
}
