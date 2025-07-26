package com.dnd.spaced.core.word.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse.PopularWordResponse;
import static org.hamcrest.Matchers.is;
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
import com.dnd.spaced.core.word.application.WordServiceFacade;
import com.dnd.spaced.core.word.application.dto.request.SearchWordRequest;
import com.dnd.spaced.core.word.application.dto.response.PopularWordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordCollectionResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse;
import com.dnd.spaced.core.word.application.dto.response.WordResponse.PronunciationResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class WordControllerTest extends CommonControllerSliceTest {

    @Autowired
    WordServiceFacade wordServiceFacade;

    @Test
    void 용어_조회_요청_성공_테스트() throws Exception {
        // given
        WordResponse wordResponse = new WordResponse(
                3L,
                "Authorization",
                "개발",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다."),
                List.of(new PronunciationResponse("어써라이제이션", "한글 발음")),
                1L,
                1L
        );

        given(wordServiceFacade.readWord(anyLong())).willReturn(wordResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/{wordId}", 3L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").value(wordResponse.id()),
                jsonPath("name").value(wordResponse.name()),
                jsonPath("category").value(wordResponse.category()),
                jsonPath("meaning").value(wordResponse.meaning()),
                jsonPath("examples").exists(),
                jsonPath("examples[0]").value(wordResponse.examples().get(0)),
                jsonPath("pronunciations").exists(),
                jsonPath("pronunciations[0].pronunciation").value(wordResponse.pronunciations().get(0).pronunciation()),
                jsonPath("pronunciations[0].type").value(wordResponse.pronunciations().get(0).type()),
                jsonPath("viewCount").value(wordResponse.viewCount()),
                jsonPath("bookmarkCount").value(wordResponse.bookmarkCount())
        );

        용어_조회_요청_문서화(resultActions);
    }

    private void 용어_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("wordId").description("조회하고자 하는 용어 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("용어 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("용어 이름"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("용어 카테고리"),
                                fieldWithPath("meaning").type(JsonFieldType.STRING).description("용어 뜻"),
                                fieldWithPath("examples").type(JsonFieldType.ARRAY).description("용어 예문"),
                                fieldWithPath("pronunciations").type(JsonFieldType.ARRAY).description("용어 발음"),
                                fieldWithPath("pronunciations[*].pronunciation").type(JsonFieldType.STRING).description("발음 내용"),
                                fieldWithPath("pronunciations[*].type").type(JsonFieldType.STRING).description("발음 타입"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 수")
                        )
                )
        );
    }

    @Test
    void 용어_검색_요청_성공_테스트() throws Exception {
        // given
        WordResponse wordResponse = new WordResponse(
                3L,
                "Authorization",
                "개발",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다."),
                List.of(new PronunciationResponse("어써라이제이션", "한글 발음")),
                1L,
                1L
        );
        WordCollectionResponse wordCollectionResponse = new WordCollectionResponse(List.of(wordResponse), wordResponse.name());
        given(wordServiceFacade.searchWord(any(SearchWordRequest.class), any(Pageable.class))).willReturn(wordCollectionResponse);

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
                jsonPath("words[0].id").value(wordResponse.id()),
                jsonPath("words[0].name").value(wordResponse.name()),
                jsonPath("words[0].category").value(wordResponse.category()),
                jsonPath("words[0].meaning").value(wordResponse.meaning()),
                jsonPath("words[0].examples").exists(),
                jsonPath("words[0].examples[0]").value(wordResponse.examples().get(0)),
                jsonPath("words[0].pronunciations").exists(),
                jsonPath("words[0].pronunciations[0].pronunciation").value(wordResponse.pronunciations().get(0).pronunciation()),
                jsonPath("words[0].pronunciations[0].type").value(wordResponse.pronunciations().get(0).type()),
                jsonPath("words[0].viewCount").value(wordResponse.viewCount()),
                jsonPath("words[0].bookmarkCount").value(wordResponse.bookmarkCount())
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
                                fieldWithPath("words").type(JsonFieldType.ARRAY).description("검색 결과 용어 목록"),
                                fieldWithPath("words[*].id").type(JsonFieldType.NUMBER).description("용어 ID"),
                                fieldWithPath("words[*].name").type(JsonFieldType.STRING).description("용어 이름"),
                                fieldWithPath("words[*].category").type(JsonFieldType.STRING).description("용어 카테고리"),
                                fieldWithPath("words[*].meaning").type(JsonFieldType.STRING).description("용어 뜻"),
                                fieldWithPath("words[*].examples").type(JsonFieldType.ARRAY).description("용어 예문"),
                                fieldWithPath("words[*].pronunciations").type(JsonFieldType.ARRAY).description("용어 발음"),
                                fieldWithPath("words[*].pronunciations[*].pronunciation").type(JsonFieldType.STRING).description("발음 내용"),
                                fieldWithPath("words[*].pronunciations[*].type").type(JsonFieldType.STRING).description("발음 타입"),
                                fieldWithPath("words[*].viewCount").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("words[*].bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 수"),
                                fieldWithPath("lastWordName").type(JsonFieldType.STRING).description("마지막으로 조회한 용어 이름")
                        )
                )
        );
    }

    @Test
    void 용어_목록_조회_요청_성공_테스트() throws Exception {
        // given
        WordResponse wordResponse = new WordResponse(
                3L,
                "Authorization",
                "개발",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다."),
                List.of(new PronunciationResponse("어써라이제이션", "한글 발음")),
                1L,
                1L
        );
        WordCollectionResponse wordCollectionResponse = new WordCollectionResponse(List.of(wordResponse), wordResponse.name());
        given(wordServiceFacade.readWords(any(), any())).willReturn(wordCollectionResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words").accept(MediaType.APPLICATION_JSON)
                             .queryParam("categoryName", "개발")
                             .queryParam("lastWordName", "Agile")
        ).andExpectAll(
                status().isOk(),
                jsonPath("words").exists(),
                jsonPath("words[0].id").value(wordResponse.id()),
                jsonPath("words[0].name").value(wordResponse.name()),
                jsonPath("words[0].category").value(wordResponse.category()),
                jsonPath("words[0].meaning").value(wordResponse.meaning()),
                jsonPath("words[0].examples").exists(),
                jsonPath("words[0].examples[0]").value(wordResponse.examples().get(0)),
                jsonPath("words[0].pronunciations").exists(),
                jsonPath("words[0].pronunciations[0].pronunciation").value(wordResponse.pronunciations().get(0).pronunciation()),
                jsonPath("words[0].pronunciations[0].type").value(wordResponse.pronunciations().get(0).type()),
                jsonPath("words[0].viewCount").value(wordResponse.viewCount()),
                jsonPath("words[0].bookmarkCount").value(wordResponse.bookmarkCount())
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
                                fieldWithPath("words").type(JsonFieldType.ARRAY).description("검색 결과 용어 목록"),
                                fieldWithPath("words[*].id").type(JsonFieldType.NUMBER).description("용어 ID"),
                                fieldWithPath("words[*].name").type(JsonFieldType.STRING).description("용어 이름"),
                                fieldWithPath("words[*].category").type(JsonFieldType.STRING).description("용어 카테고리"),
                                fieldWithPath("words[*].meaning").type(JsonFieldType.STRING).description("용어 뜻"),
                                fieldWithPath("words[*].examples").type(JsonFieldType.ARRAY).description("용어 예문"),
                                fieldWithPath("words[*].pronunciations").type(JsonFieldType.ARRAY).description("용어 발음"),
                                fieldWithPath("words[*].pronunciations[*].pronunciation").type(JsonFieldType.STRING).description("발음 내용"),
                                fieldWithPath("words[*].pronunciations[*].type").type(JsonFieldType.STRING).description("발음 타입"),
                                fieldWithPath("words[*].viewCount").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("words[*].bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 수"),
                                fieldWithPath("lastWordName").type(JsonFieldType.STRING).description("마지막으로 조회한 용어 이름")
                        )
                )
        );
    }

    @Test
    void 많이_찾아본_용어_목록_조회_요청_성공_테스트() throws Exception {
        // given
        PopularWordResponse popularWordResponse = new PopularWordResponse(1, 3L, "Authorization");
        PopularWordCollectionResponse popularWordCollectionResponse = new PopularWordCollectionResponse(List.of(popularWordResponse));
        given(wordServiceFacade.readPopularWords()).willReturn(popularWordCollectionResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/words/popular").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("popularWords").exists(),
                jsonPath("popularWords[0].rank", is(1L), Long.class),
                jsonPath("popularWords[0].wordId", is(3L), Long.class),
                jsonPath("popularWords[0].name").value("Authorization")
        );

        많이_찾아본_용어_목록_조회_요청_문서화(resultActions);
    }

    private void 많이_찾아본_용어_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("popularWords").description("많이 조회한 용어 검색 결과"),
                                fieldWithPath("popularWords[*].rank").description("많이 조회한 용어 랭킹"),
                                fieldWithPath("popularWords[*].wordId").description("용어 ID"),
                                fieldWithPath("popularWords[*].name").description("용어 이름")
                        )
                )
        );
    }
}
