package com.dnd.spaced.core.report.presentation;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.report.application.dto.request.ReportRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class ReportControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 신고_요청_성공_테스트() throws Exception {
        // when & then
        ReportRequest request = new ReportRequest(1L, "기타");

        mockMvc.perform(
                post("/reports").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());
    }
}
