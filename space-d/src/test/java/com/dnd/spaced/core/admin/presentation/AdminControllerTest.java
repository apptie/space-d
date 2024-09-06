package com.dnd.spaced.core.admin.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.presentation.dto.request.SaveWordRequest;
import com.dnd.spaced.core.admin.presentation.dto.request.SaveWordRequest.PronunciationInfoRequest;
import com.dnd.spaced.core.admin.presentation.dto.request.UpdateBlacklistTokenRequest;
import com.dnd.spaced.core.admin.presentation.dto.request.UpdateWordExampleRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class AdminControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void registerBlacklistToken_성공_테스트() throws Exception {
        // given
        UpdateBlacklistTokenRequest request = new UpdateBlacklistTokenRequest("id");

        // when & then
        ResultActions resultAction = mockMvc.perform(
                post("/admin/blacklist-token").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        );

        registerBlacklistToken_문서화(resultAction);
    }

    private void registerBlacklistToken_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 관리자 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("accountId").attributes(field("constraints", "가입한 회원 ID만 가능"))
                                                          .description("블랙리스트 토큰으로 등록할 회원 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveWord_성공_테스트() throws Exception {
        // given
        List<PronunciationInfoRequest> pronunciationInfo = List.of(new PronunciationInfoRequest("pronunciation", "typeName"));
        List<String> example = List.of("example");
        SaveWordRequest request = new SaveWordRequest("name", "meaning", "categoryName", pronunciationInfo, example);

        given(adminWordService.saveWord(any(SaveWordDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(
                post("/admin/words").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().stringValues("Location", "/words/1")
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateWordExample_성공_테스트() throws Exception {
        // given
        UpdateWordExampleRequest request = new UpdateWordExampleRequest("example");

        // when & then
        mockMvc.perform(
                patch("/admin/words/examples/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteWordExample_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/admin/words/examples/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deletePronunciation_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/admin/words/pronunciations/{id}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );
    }
}
