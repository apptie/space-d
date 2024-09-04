package com.dnd.spaced.core.auth.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
                                fieldWithPath("jobGroupName").attributes(field("constraints", generateLinkCode(DocsUrl.JOB_GROUP))).description("회원 직군"),
                                fieldWithPath("companyName").attributes(field("constraints", generateLinkCode(DocsUrl.COMPANY))).description("회원 회사 종류"),
                                fieldWithPath("experienceName").attributes(field("constraints", generateLinkCode(DocsUrl.EXPERIENCE))).description("회원 경력")
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
        ResultActions resultActions = mockMvc.perform(
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

        refreshToken_문서화(resultActions);
    }

    @Test
    void refreshToken_실패_테스트_refreshToken_없음() throws Exception {
        // given
        Cookie notRefreshTokenCookie = mock(Cookie.class);

        given(notRefreshTokenCookie.getName()).willReturn("not refreshToken");

        // when & then
        mockMvc.perform(
                post("/auths/refresh-token").contentType(MediaType.APPLICATION_JSON)
                        .cookie(notRefreshTokenCookie)
        ).andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.code").value("REFRESH_TOKEN_NOT_FOUND"),
                jsonPath("$.message").value("refresh token cookie가 없습니다.")
        );
    }

    private void refreshToken_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestCookies(
                                cookieWithName("refreshToken").description("토큰 재발급을 위한 refresh token cookie")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("새롭게 발급된 refresh token")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("새롭게 발급된 access token"),
                                fieldWithPath("tokenScheme").description("토큰 scheme")
                        )
                )
        );
    }
}
