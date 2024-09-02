package com.dnd.spaced.core.auth.presentation;

import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.auth.presentation.dto.request.UpdateAccountCareerInfoRequest;
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
        mockMvc.perform(
                post("/auths/profile").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );
    }
}
