package com.dnd.spaced.core.account.application.dto.response;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.CareerInfo;
import com.dnd.spaced.core.account.domain.ProfileInfo;

public record AccountInfoDto(
        String nickname,
        String profileImage,
        String jobGroupName,
        String companyName,
        String experienceName
) {

    public static AccountInfoDto from(Account account) {
        ProfileInfo profileInfo = account.getProfileInfo();
        CareerInfo careerInfo = account.getCareerInfo();

        return new AccountInfoDto(
                profileInfo.getNickname(),
                profileInfo.getProfileImage(),
                careerInfo.getJobGroup().getName(),
                careerInfo.getCompany().getName(),
                careerInfo.getExperience().getName()
        );
    }
}
