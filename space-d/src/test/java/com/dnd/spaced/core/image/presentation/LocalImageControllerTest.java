package com.dnd.spaced.core.image.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class LocalImageControllerTest extends CommonControllerSliceTest {

    @Test
    void 로컬_이미지_요청_성공_테스트() throws Exception {
        // given
        given(localImageService.readImage(anyString())).willReturn(mock(UrlResource.class));

        // when
        mockMvc.perform(get("/images/{imageName}", "earth.png"))
               .andExpectAll(
                       status().isOk(),
                       header().string("Content-Type", "image/png;charset=UTF-8")
               );
    }
}
