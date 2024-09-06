package com.dnd.spaced.core.word.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.word.application.dto.request.SearchConditionDto;
import com.dnd.spaced.core.word.application.dto.response.ReadAllWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto.WordPronunciationInfoDto;
import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import com.dnd.spaced.core.word.presentation.dto.request.ReadWordAllRequest;
import com.dnd.spaced.core.word.presentation.dto.request.SearchWordRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
class WordControllerTest extends CommonControllerSliceTest {

    @Test
    void read_성공_테스트() throws Exception {
        // given
        ReadWordDto readWordDto = new ReadWordDto(
                1L,
                "name",
                "categoryName",
                "meaning",
                List.of("examples"),
                List.of(new WordPronunciationInfoDto("pronunciation", "typeName")),
                1L
        );

        given(wordService.read(anyLong())).willReturn(readWordDto);

        // when & then
        mockMvc.perform(
                get("/words/{id}", 1L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").value(readWordDto.id()),
                jsonPath("name").value(readWordDto.name()),
                jsonPath("categoryName").value(readWordDto.categoryName()),
                jsonPath("meaning").value(readWordDto.meaning()),
                jsonPath("examples").exists(),
                jsonPath("pronunciationInfo").exists(),
                jsonPath("viewCount").value(readWordDto.id())
        );
    }

    @Test
    void search_성공_테스트() throws Exception {
        // given
        SearchWordRequest request = new SearchWordRequest(
                "name",
                "categoryName",
                "pronunciation",
                "lastWordName"
        );
        SearchedWordDto searchedWordDto = new SearchedWordDto(
                1L,
                "name",
                "meaning",
                "category",
                1L
        );
        given(wordService.search(any(SearchConditionDto.class))).willReturn(List.of(searchedWordDto));

        // when & then
        mockMvc.perform(
                get("/words/search").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(searchedWordDto.name()),
                jsonPath("words[*].meaning").value(searchedWordDto.meaning()),
                jsonPath("words[*].category").value(searchedWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );
    }

    @Test
    void readAllBy_성공_테스트() throws Exception {
        // given
        ReadWordAllRequest request = new ReadWordAllRequest("categoryName", "lastWordName");
        ReadAllWordDto readAllWordDto = new ReadAllWordDto(
                1L,
                "name",
                "meaning",
                "category",
                1L
        );

        given(wordService.readAllBy(any(), any(), any())).willReturn(List.of(readAllWordDto));

        // when & then
        mockMvc.perform(
                get("/words").contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(readAllWordDto.name()),
                jsonPath("words[*].meaning").value(readAllWordDto.meaning()),
                jsonPath("words[*].category").value(readAllWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );
    }
}
