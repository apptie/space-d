package com.dnd.spaced.core.account.application.dto.response;

public record AccountResponse(
        String nickname,
        String profileImage,
        String jobGroupName,
        String companyName,
        String experienceName
) {
}
