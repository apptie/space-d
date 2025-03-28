package com.dnd.spaced.config.docs.snippet.exceptions.skill;

import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record SkillExceptionDocs(Map<String, ExceptionContent> readSkillException) {
}
