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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class WordControllerTest extends CommonControllerSliceTest {

    @Test
    void 용어_조회_요청_성공_테스트() throws Exception {
        // given
        ReadWordDto readWordDto = new ReadWordDto(
                3L,
                "Authorization",
                "개발",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다."),
                List.of(new WordPronunciationInfoDto("어써라이제이션", "한글 발음")),
                1L
        );

        given(wordService.read(anyLong())).willReturn(readWordDto);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/{id}", 3L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").value(readWordDto.id()),
                jsonPath("name").value(readWordDto.name()),
                jsonPath("categoryName").value(readWordDto.categoryName()),
                jsonPath("meaning").value(readWordDto.meaning()),
                jsonPath("examples").exists(),
                jsonPath("pronunciationInfo").exists(),
                jsonPath("viewCount").value(readWordDto.viewCount())
        );

        용어_조회_문서화(resultActions);
    }

    private void 용어_조회_문서화(ResultActions resultActions) throws Exception {
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
    void 용어_검색_요청_성공_테스트() throws Exception {
        // given
        SearchedWordDto searchedWordDto = new SearchedWordDto(
                3L,
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                1L
        );
        given(wordService.search(any(SearchConditionDto.class))).willReturn(List.of(searchedWordDto));

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/search").accept(MediaType.APPLICATION_JSON)
                                    .queryParam("name", "Authorization")
                                    .queryParam("categoryName", "개발")
                                    .queryParam("pronunciation", "어써라이제이션")
                                    .queryParam("lastWordName", "Agile")
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(searchedWordDto.name()),
                jsonPath("words[*].meaning").value(searchedWordDto.meaning()),
                jsonPath("words[*].category").value(searchedWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );

        용어_검색_요청_문서화(resultActions);
    }

    private void 용어_검색_요청_문서화(ResultActions resultActions) throws Exception {
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
    void 용어_목록_조회_요청_성공_테스트() throws Exception {
        // given
        ReadAllWordDto readAllWordDto = new ReadAllWordDto(
                3L,
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                "개발",
                1L
        );

        given(wordService.readAllBy(any(), any(), any())).willReturn(List.of(readAllWordDto));

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words").accept(MediaType.APPLICATION_JSON)
                             .queryParam("categoryName", "개발")
                             .queryParam("lastWordName", "Agile")
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(readAllWordDto.name()),
                jsonPath("words[*].meaning").value(readAllWordDto.meaning()),
                jsonPath("words[*].category").value(readAllWordDto.category()),
                jsonPath("words[*].viewCount").exists()
        );

        용어_목록_조회_요청_문서화(resultActions);
    }

    private void 용어_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
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
    void 많이_찾아본_용어_목록_조회_요청_성공_테스트() throws Exception {
        // given
        PopularWordDto popularWordDto = new PopularWordDto(1, 3L, "Authorization");
        given(wordService.readPopularWordsAll()).willReturn(List.of(popularWordDto));

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/popular").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[*].rank").value(popularWordDto.rank()),
                jsonPath("words[*].id").exists(),
                jsonPath("words[*].name").value(popularWordDto.name())
        );

        많이_찾아본_용어_목록_조회_요청_문서화(resultActions);
    }

    private void 많이_찾아본_용어_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("words").description("많이 조회한 용어 검색 결과"),
                                fieldWithPath("words[*].rank").description("많이 조회한 용어 랭킹"),
                                fieldWithPath("words[*].id").description("용어 ID"),
                                fieldWithPath("words[*].name").description("용어 이름")
                        )
                )
        );
    }
}
