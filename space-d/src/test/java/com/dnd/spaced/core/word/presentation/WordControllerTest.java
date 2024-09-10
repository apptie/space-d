package com.dnd.spaced.core.word.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.word.application.dto.request.SearchConditionDto;
import com.dnd.spaced.core.word.application.dto.response.PopularWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadAllWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto;
import com.dnd.spaced.core.word.application.dto.response.ReadWordDto.WordPronunciationInfoDto;
import com.dnd.spaced.core.word.application.dto.response.SearchedWordDto;
import com.dnd.spaced.core.word.presentation.dto.request.ReadWordAllRequest;
import com.dnd.spaced.core.word.presentation.dto.request.SearchWordRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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
        ResultActions resultActions = mockMvc.perform(
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

        read_문서화(resultActions);
    }

    private void read_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("id").description("조회하고자 하는 용어 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("용어 ID"),
                                fieldWithPath("name").description("용어 이름"),
                                fieldWithPath("categoryName").description("용어 카테고리"),
                                fieldWithPath("meaning").description("용어 뜻"),
                                fieldWithPath("examples").description("용어 예문"),
                                fieldWithPath("pronunciationInfo").description("용어 발음"),
                                fieldWithPath("pronunciationInfo[*].pronunciation").description("발음 내용"),
                                fieldWithPath("pronunciationInfo[*].typeName").description("발음 타입"),
                                fieldWithPath("viewCount").description("조회수")
                        )
                )
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
        ResultActions resultActions = mockMvc.perform(
                get("/words/search").accept(MediaType.APPLICATION_JSON)
                                    .queryParam("name", "name")
                                    .queryParam("categoryName", "categoryName")
                                    .queryParam("pronunciation", "pronunciation")
                                    .queryParam("lastWordName", "lastWordName")
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(searchedWordDto.name()),
                jsonPath("words[*].meaning").value(searchedWordDto.meaning()),
                jsonPath("words[*].category").value(searchedWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );

        search_문서화(resultActions);
    }

    private void search_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        queryParameters(
                                parameterWithName("name").description("검색할 용어 이름").optional(),
                                parameterWithName("categoryName").attributes(
                                                                     field("constraints", generateLinkCode(DocsUrl.CATEGORY)))
                                                             .description("검색할 용어 카테고리").optional(),
                                parameterWithName("pronunciation").description("검색할 용어 발음").optional(),
                                parameterWithName("lastWordName").description("마지막으로 조회한 용어 이름").optional()
                        ),
                        responseFields(
                                fieldWithPath("words").description("용어 검색 결과"),
                                fieldWithPath("words[*].id").description("용어 ID"),
                                fieldWithPath("words[*].name").description("용어 이름"),
                                fieldWithPath("words[*].meaning").description("용어 뜻"),
                                fieldWithPath("words[*].category").description("용어 카테고리"),
                                fieldWithPath("words[*].viewCount").description("조회수")
                        )
                )
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
        ResultActions resultActions = mockMvc.perform(
                get("/words").accept(MediaType.APPLICATION_JSON)
                             .queryParam("categoryName", "categoryName")
                             .queryParam("lastWordName", "lastWordName")
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(readAllWordDto.name()),
                jsonPath("words[*].meaning").value(readAllWordDto.meaning()),
                jsonPath("words[*].category").value(readAllWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );

        readAllBy_문서화(resultActions);
    }

    private void readAllBy_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        queryParameters(
                                parameterWithName("categoryName").attributes(field("constraints", generateLinkCode(DocsUrl.CATEGORY)))
                                                             .description("필터링할 용어 카테고리").optional(),
                                parameterWithName("lastWordName").description("마지막으로 조회한 용어 이름").optional()
                        ),
                        responseFields(
                                fieldWithPath("words").description("용어 검색 결과"),
                                fieldWithPath("words[*].id").description("용어 ID"),
                                fieldWithPath("words[*].name").description("용어 이름"),
                                fieldWithPath("words[*].meaning").description("용어 뜻"),
                                fieldWithPath("words[*].category").description("용어 카테고리"),
                                fieldWithPath("words[*].viewCount").description("조회수")
                        )
                )
        );
    }

    @Test
    void readPopularWordsAll_성공_테스트() throws Exception {
        // given
        PopularWordDto popularWordDto = new PopularWordDto(1, 1L, "name");
        given(wordService.readPopularWordsAll()).willReturn(List.of(popularWordDto));

        // when & then
        mockMvc.perform(
                get("/words/popular").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(popularWordDto.name())
        );
    }
}
