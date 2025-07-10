package com.dnd.spaced.core.account.application.dto.mapper;

import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.Career;
import com.dnd.spaced.core.account.domain.embed.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountResponseMapper {

    public static AccountResponse toDto(Account account) {
        Profile profile = account.getProfile();
        Career career = account.getCareer();

        return new AccountResponse(
                profile.getNickname(),
                profile.getProfileImageName(),
                career.getJobGroup().getName(),
                career.getCompany().getName(),
                career.getExperience().getName()
        );
    }
}
