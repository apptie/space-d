package com.dnd.spaced.core.skill.application.event.listener.exception;

import com.dnd.spaced.global.exception.base.SkillServerException;
import com.dnd.spaced.global.exception.code.SkillErrorCode;

public class SkillNotFoundException extends SkillServerException {

    public SkillNotFoundException(String message) {
        super(SkillErrorCode.SKILL_NOT_FOUND_EXCEPTION, message);
    }
}
