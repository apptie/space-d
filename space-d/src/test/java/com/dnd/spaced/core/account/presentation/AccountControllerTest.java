package com.dnd.spaced.core.account.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateCareerInfoRequest;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateProfileInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class AccountControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("account")
    void withdrawal_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/accounts/withdrawal").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser("account")
    void changeCareerInfo_성공_테스트() throws Exception {
        // given
        UpdateCareerInfoRequest request = new UpdateCareerInfoRequest("jobGroup", "company", "experienceName");

        // when & then
        mockMvc.perform(
                put("/accounts/career-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser("account")
    void changeProfileInfo_성공_테스트() throws Exception {
        // given
        UpdateProfileInfoRequest request = new UpdateProfileInfoRequest("originNickname", "profileImageKoreanName");

        // when & then
        mockMvc.perform(
                put("/accounts/profile-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser("account")
    void findAccountInfo_성공_테스트() throws Exception {
        // given
        AccountInfoDto accountInfoDto = new AccountInfoDto(
                "nickname",
                "profileImage",
                "jobGroup",
                "company",
                "experience"
        );

        given(accountService.findAccountInfo(anyString())).willReturn(accountInfoDto);

        // when
        mockMvc.perform(
                get("/accounts").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.nickname").value(accountInfoDto.nickname()),
                jsonPath("$.profileImage").value(accountInfoDto.profileImage()),
                jsonPath("$.jobGroupName").value(accountInfoDto.jobGroupName()),
                jsonPath("$.companyName").value(accountInfoDto.companyName()),
                jsonPath("$.experienceName").value(accountInfoDto.experienceName())
        );
    }
}
