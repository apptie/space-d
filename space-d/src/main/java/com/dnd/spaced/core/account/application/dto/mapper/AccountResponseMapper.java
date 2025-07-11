package com.dnd.spaced.core.account.application.dto.mapper;

import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.account.domain.embed.Career;
import com.dnd.spaced.core.account.domain.embed.Profile;
import com.dnd.spaced.global.mapper.Mapper;

@Mapper
public class AccountResponseMapper {

    public AccountResponse toDto(Account account) {
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
