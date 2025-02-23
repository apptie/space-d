package com.dnd.spaced.core.account.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
import com.dnd.spaced.core.account.application.dto.response.AccountInfoDto;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateCareerInfoRequest;
import com.dnd.spaced.core.account.presentation.dto.request.UpdateProfileInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AccountControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 회원_탈퇴_요청_성공_테스트() throws Exception {
        // when & then
        ResultActions resultActions = mockMvc.perform(
                delete("/accounts/withdrawal").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );

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
        UpdateCareerInfoRequest request = new UpdateCareerInfoRequest("개발자", "중소기업", "비공개");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                put("/accounts/career-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        회원_경력_정보_변경_요청_문서화(resultActions);
    }

    private void 회원_경력_정보_변경_요청_문서화(ResultActions resultActions) throws Exception {
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
    @WithMockUser("1")
    void 회원_프로필_정보_변경_요청_성공_테스트() throws Exception {
        // given
        UpdateProfileInfoRequest request = new UpdateProfileInfoRequest("행복한금성001", "금성");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                put("/accounts/profile-info").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );

        회원_프로필_정보_변경_요청_문서화(resultActions);
    }

    private void 회원_프로필_정보_변경_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("nickname").attributes(field("constraints", "기존 닉네임 입력")).description("회원 직군"),
                                fieldWithPath("profileImageKoreanName").attributes(field("constraints", generateLinkCode(DocsUrl.PROFILE_IMAGE_NAME))).description("회원 회사 종류")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원_정보_조회_요청_성공_테스트() throws Exception {
        // given
        AccountInfoDto accountInfoDto = new AccountInfoDto(
                "재빠른지구001",
                "earth.png",
                "개발자",
                "비공개",
                "1~2년 차"
        );

        given(accountService.findAccountInfo(anyLong())).willReturn(accountInfoDto);

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
