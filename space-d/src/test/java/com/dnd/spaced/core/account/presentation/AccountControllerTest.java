package com.dnd.spaced.core.account.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.account.application.AccountService;
import com.dnd.spaced.core.account.application.dto.request.ChangeCareerRequest;
import com.dnd.spaced.core.account.application.dto.request.ChangeProfileRequest;
import com.dnd.spaced.core.account.application.dto.response.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AccountControllerTest extends CommonControllerSliceTest {

    @Autowired
    AccountService accountService;

    @Test
    @WithMockUser("1")
    void 회원_탈퇴_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                delete("/accounts/withdrawal").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

        verify(accountService).withdrawal(anyLong());

        회원_탈퇴_요청_문서화(resultActions);
    }

    private void 회원_탈퇴_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원_경력_정보_변경_요청_성공_테스트() throws Exception {
        // given
        ChangeCareerRequest request = new ChangeCareerRequest("개발자", "중소기업", "비공개");

        // when & then

        ResultActions resultActions = mockMvc.perform(
                put("/accounts/career-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        verify(accountService).changeCareerInfo(anyLong(), any(ChangeCareerRequest.class));

        회원_경력_정보_변경_요청_문서화(resultActions);
    }

    private void 회원_경력_정보_변경_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("changedJobGroupName").attributes(field("constraints", generateLinkCode(DocsUrl.JOB_GROUP))).description("회원 직군"),
                                fieldWithPath("changedCompanyName").attributes(field("constraints", generateLinkCode(DocsUrl.COMPANY))).description("회원 회사 종류"),
                                fieldWithPath("changedExperienceName").attributes(field("constraints", generateLinkCode(DocsUrl.EXPERIENCE))).description("회원 경력")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원_프로필_정보_변경_요청_성공_테스트() throws Exception {
        // given
        ChangeProfileRequest request = new ChangeProfileRequest("행복한금성001", "금성");

        // when & then

        ResultActions resultActions = mockMvc.perform(
                put("/accounts/profile-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        verify(accountService).changeProfileInfo(anyLong(), any(ChangeProfileRequest.class));

        회원_프로필_정보_변경_요청_문서화(resultActions);
    }

    private void 회원_프로필_정보_변경_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("changedNickname").attributes(field("constraints", "기존 닉네임 입력")).description("회원 직군"),
                                fieldWithPath("changedProfileImageKoreanName").attributes(field("constraints", generateLinkCode(DocsUrl.PROFILE_IMAGE_NAME))).description("회원 회사 종류")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원_정보_조회_요청_성공_테스트() throws Exception {
        // given
        AccountResponse accountResponse = new AccountResponse(
                "재빠른지구001",
                "earth.png",
                "개발자",
                "비공개",
                "1~2년 차"
        );

        given(accountService.readAccount(anyLong())).willReturn(accountResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/accounts").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.nickname").value("재빠른지구001"),
                jsonPath("$.profileImage").value("earth.png"),
                jsonPath("$.jobGroupName").value("개발자"),
                jsonPath("$.companyName").value("비공개"),
                jsonPath("$.experienceName").value("1~2년 차")
        );

        verify(accountService).readAccount(anyLong());

        회원_정보_조회_요청_문서화(resultActions);
    }

    private void 회원_정보_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        responseFields(
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("jobGroupName").description("회원 직군"),
                                fieldWithPath("companyName").description("회원 회사 정보"),
                                fieldWithPath("experienceName").description("회원 경력")
                        )
                )
        );
    }
}
