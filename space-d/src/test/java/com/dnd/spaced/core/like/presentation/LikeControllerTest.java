package com.dnd.spaced.core.like.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class LikeControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("account")
    void processLike_성공_테스트() throws Exception {
        // when & then
        mockMvc.perform(
                post("/comments/{commentId}/likes", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isNoContent()
        );
    }
}
