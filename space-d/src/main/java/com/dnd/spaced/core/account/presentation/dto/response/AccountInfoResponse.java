package com.dnd.spaced.core.account.presentation.dto.response;

import com.dnd.spaced.core.account.application.dto.response.AccountResponse;

public record AccountInfoResponse(
        String nickname,
        String profileImage,
        String jobGroupName,
        String companyName,
        String experienceName
) {

    public static AccountInfoResponse from(AccountResponse dto) {
        return new AccountInfoResponse(
                dto.nickname(),
                dto.profileImage(),
                dto.jobGroupName(),
                dto.companyName(),
                dto.experienceName()
        );
    }
}
