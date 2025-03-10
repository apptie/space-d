package com.dnd.spaced.core.account.application.dto.mapper;

import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.CareerInfo;
import com.dnd.spaced.core.account.domain.embed.ProfileInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountApplicationMapper {

    public static AccountResponse toDto(Account account) {
        ProfileInfo profileInfo = account.getProfileInfo();
        CareerInfo careerInfo = account.getCareerInfo();

        return new AccountResponse(
                profileInfo.getNickname(),
                profileInfo.getProfileImage(),
                careerInfo.getJobGroup().getName(),
                careerInfo.getCompany().getName(),
                careerInfo.getExperience().getName()
        );
    }
}
