package com.dnd.spaced.core.auth.presentation;

import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.presentation.dto.request.UpdateAccountCareerInfoRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AuthControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("account")
    void initAccountProfile_성공_테스트() throws Exception {
        // given
        willDoNothing().given(initAccountInfoService)
                       .initCareerInfo(anyString(), anyString(), anyString(), anyString());

        UpdateAccountCareerInfoRequest request = new UpdateAccountCareerInfoRequest(
                "jobGroupName",
                "companyName",
                "experienceName"
        );

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/auths/profile").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        initAccountProfile_문서화(resultActions);
    }

    private void initAccountProfile_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("jobGroupName").description(generateLinkCode(DocsUrl.JOB_GROUP)),
                                fieldWithPath("companyName").description(generateLinkCode(DocsUrl.COMPANY)),
                                fieldWithPath("experienceName").description(generateLinkCode(DocsUrl.EXPERIENCE))
                        )
                )
        );
    }

    @Test
    void refreshToken_성공_테스트() throws Exception {
        // given
        Cookie refreshTokenCookie = mock(Cookie.class);

        given(refreshTokenCookie.getName()).willReturn("refreshToken");
        given(refreshTokenCookie.getValue()).willReturn("Bearer refreshToken");
        given(authService.refreshToken(anyString())).willReturn(
                new TokenDto("accessToken", "refreshToken", "BEARER")
        );

        // when & then
        mockMvc.perform(
                post("/auths/refresh-token").contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshTokenCookie)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.accessToken").value("accessToken"),
                jsonPath("$.tokenScheme").value("BEARER"),
                cookie().value("refreshToken", "refreshToken"),
                cookie().path("refreshToken", "/"),
                cookie().secure("refreshToken", true),
                cookie().httpOnly("refreshToken", true)
        );
    }
}
