package com.dnd.spaced.core.account.presentation.dto.response;

import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;

public record AccountInfoResponse(
        String nickname,
        String profileImage,
        String jobGroupName,
        String companyName,
        String experienceName
) {

    public static AccountInfoResponse from(AccountInfoDto dto) {
        return new AccountInfoResponse(
                dto.nickname(),
                dto.profileImage(),
                dto.jobGroupName(),
                dto.companyName(),
                dto.experienceName()
        );
    }
}
