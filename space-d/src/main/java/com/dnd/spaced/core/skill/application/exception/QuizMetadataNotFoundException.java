package com.dnd.spaced.core.skill.application.exception;

import com.dnd.spaced.global.exception.base.SkillServerException;
import com.dnd.spaced.global.exception.code.SkillErrorCode;

public class QuizMetadataNotFoundException extends SkillServerException {

    public QuizMetadataNotFoundException(String message) {
        super(SkillErrorCode.QUIZ_METADATA_NOT_FOUND_EXCEPTION, message);
    }
}
