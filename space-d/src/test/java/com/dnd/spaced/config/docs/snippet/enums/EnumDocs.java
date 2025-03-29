package com.dnd.spaced.config.docs.snippet.enums;

import java.util.Map;
import lombok.Builder;

@Builder
public record EnumDocs (
        Map<String, String> jobGroup,
        Map<String, String> company,
        Map<String, String> experience,
        Map<String, String> profileImageName,
        Map<String, String> category,
        Map<String, String> pronunciationType,
        Map<String, String> quizCategory,
        Map<String, String> reportReason,
        Map<String, String> reportStatus,
        Map<String, String> todayQuizStatus
) {
}
