package com.dnd.spaced.core.quiz.presentation;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static com.dnd.spaced.config.docs.link.DocumentLinkGenerator.generateLinkCode;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.link.DocumentLinkGenerator.DocsUrl;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.SimpleTodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class TodayQuizControllerTest extends CommonControllerSliceTest {

    @Test
    void 최신_오늘의_퀴즈_요청_성공_테스트() throws Exception {
        // given
        SimpleTodayQuizResponse.TodayQuizQuestionResponse todayQuizQuestionResponse = new SimpleTodayQuizResponse.TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"
        );
        SimpleTodayQuizResponse todayQuizResponse = new SimpleTodayQuizResponse(
                1L,
                todayQuizQuestionResponse,
                LocalDateTime.now()
        );

        given(todayQuizService.readLatestTodayQuiz()).willReturn(todayQuizResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/today-quizzes/latest").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").exists(),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.passage").value(
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
        );

        verify(todayQuizService).readLatestTodayQuiz();

        최신_오늘의_퀴즈_요청_문서화(resultActions);
    }

    private void 최신_오늘의_퀴즈_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("id").description("오늘의 퀴즈 id")
                                                   .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion").description("오늘의 퀴즈 문제")
                                                                  .type(JsonFieldType.OBJECT),
                                fieldWithPath("todayQuizQuestion.quizCategory").description("오늘의 퀴즈 유형")
                                                                               .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.question").description("오늘의 퀴즈 문제")
                                                                           .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.passage").description("오늘의 퀴즈 문제 지문")
                                                                                  .type(JsonFieldType.STRING),
                                fieldWithPath("createdAt").description("오늘의 퀴즈 생성 시간")
                                                          .type(JsonFieldType.STRING)
                        )
                )
        );
    }

    @Test
    void 오늘의_퀴즈_조회_요청_성공_테스트() throws Exception {
        // given
        TodayQuizOptionResponse authorizationOption = new TodayQuizOptionResponse(1L, 1L, "Authorization", 1);
        TodayQuizOptionResponse controllerOption = new TodayQuizOptionResponse(2L, 2L, "Controller", 2);
        TodayQuizOptionResponse domainOption = new TodayQuizOptionResponse(3L, 3L, "Domain", 3);
        TodayQuizOptionResponse repositoryOption = new TodayQuizOptionResponse(4L, 4L, "Repository", 4);
        TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of(authorizationOption, controllerOption, domainOption, repositoryOption),
                1L,
                "Authorization"
        );
        TodayQuizResponse todayQuizResponse = new TodayQuizResponse(
                1L,
                todayQuizQuestionResponse,
                TodayQuizStatus.NOT_SOLVED
        );

        given(todayQuizService.readTodayQuiz(anyLong(), anyLong())).willReturn(todayQuizResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/today-quizzes/{todayQuizId}", 1L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").exists(),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.passage").value(
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("todayQuizQuestion.todayQuizOptions").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].id").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].content").exists()
        );

        verify(todayQuizService).readTodayQuiz(anyLong(), anyLong());

        오늘의_퀴즈_조회_요청_문서화(resultActions);
    }

    private void 오늘의_퀴즈_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("id").description("오늘의 퀴즈 id")
                                                   .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion").description("오늘의 퀴즈 문제")
                                                                  .type(JsonFieldType.OBJECT),
                                fieldWithPath("todayQuizQuestion.quizCategory").description("오늘의 퀴즈 유형")
                                                                               .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.question").description("오늘의 퀴즈 문제")
                                                                           .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.passage").description("오늘의 퀴즈 문제 지문")
                                                                                  .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.todayQuizOptions").description("오늘의 퀴즈 문제 보기")
                                                                                   .type(JsonFieldType.ARRAY),
                                fieldWithPath("todayQuizQuestion.todayQuizOptions[*].id").description("오늘의 퀴즈 문제 보기 id")
                                                                                         .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion.todayQuizOptions[*].content").description(
                                                                                                      "오늘의 퀴즈 문제 보기 내용")
                                                                                              .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.todayQuizOptions[*].wordId").description(
                                                                                                      "오늘의 퀴즈 문제 정답 용어 ID")
                                                                                              .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion.todayQuizOptions[*].optionOrder").description(
                                                                                                     "오늘의 퀴즈 문제 순서")
                                                                                             .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion.answerWordId").description("오늘의 퀴즈 문제 용어 답 id")
                                                                               .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion.answerContent").description("오늘의 퀴즈 문제 용어 답 내용")
                                                                               .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizStatus").type(JsonFieldType.STRING)
                                                                .attributes(field("description",
                                                                        generateLinkCode(DocsUrl.TODAY_QUIZ_STATUS)))

                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 오늘의_퀴즈_채점_요청_성공_테스트() throws Exception {
        // given
        willDoNothing().given(todayQuizService).grade(anyLong(), anyLong(), any(GradeTodayQuizRequest.class));

        // when & then
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(1L, "Authorization");

        ResultActions resultActions = mockMvc.perform(
                post("/today-quizzes/{todayQuizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION,
                                                                               "Bearer AccessToken")
                                                                       .contentType(MediaType.APPLICATION_JSON)
                                                                       .content(
                                                                               objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/today-quizzes/1/graded-answers")
        );

        verify(todayQuizService).grade(anyLong(), anyLong(), any(GradeTodayQuizRequest.class));

        오늘의_퀴즈_채점_요청_문서화(resultActions);
    }

    private void 오늘의_퀴즈_채점_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        requestFields(
                                fieldWithPath("selectedWordId").type(JsonFieldType.NUMBER)
                                                               .description("정답으로 제출한 용어 ID"),
                                fieldWithPath("selectedContent").type(JsonFieldType.STRING)
                                                                .description("정답으로 제출한 용어 이름")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("오늘의 퀴즈 채점 결과를 확인할 수 있는 API")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원이_제출한_오늘의_퀴즈에_대한_채점_결과_목록_조회_요청_성공_테스트() throws Exception {
        // given
        TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"
        );
        TodayQuizGradedAnswerResponse todayQuizGradedAnswerResponse = new TodayQuizGradedAnswerResponse(
                1L,
                1L,
                1L,
                todayQuizQuestionResponse,
                "Authorization",
                "Authorization",
                true
        );
        TodayQuizGradedAnswerCollectionResponse response = new TodayQuizGradedAnswerCollectionResponse(
                List.of(todayQuizGradedAnswerResponse)
        );

        given(
                todayQuizService.readTodayQuizGradedAnswers(
                        anyLong(),
                        any(ReadTodayQuizGradedAnswerSearchRequest.class),
                        any(Pageable.class)
                )
        ).willReturn(response);

        // when & then
        ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                null
        );

        ResultActions resultActions = mockMvc.perform(
                get("/today-quizzes/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                jsonPath("answers").exists(),
                jsonPath("answers[0].id", is(1L), Long.class),
                jsonPath("answers[0].todayQuizId", is(1L), Long.class),
                jsonPath("answers[0].accountId", is(1L), Long.class),
                jsonPath("answers[0].todayQuizQuestion").exists(),
                jsonPath("answers[0].todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("answers[0].todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("answers[0].todayQuizQuestion.passage").value(
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("answers[0].selectedQuizOptionContent").value("Authorization"),
                jsonPath("answers[0].answerQuizOptionContent").value("Authorization")
        );

        verify(todayQuizService).readTodayQuizGradedAnswers(
                anyLong(),
                any(ReadTodayQuizGradedAnswerSearchRequest.class),
                any(Pageable.class)
        );

        회원이_제출한_오늘의_퀴즈에_대한_채점_결과_목록_조회_요청_문서화(resultActions);
    }

    private void 회원이_제출한_오늘의_퀴즈에_대한_채점_결과_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        responseFields(
                                fieldWithPath("answers").description("회원이 제출한 오늘의 퀴즈 채점 결과")
                                                        .type(JsonFieldType.ARRAY),
                                fieldWithPath("answers[*].id").description("채점 ID")
                                                              .type(JsonFieldType.NUMBER),
                                fieldWithPath("answers[*].todayQuizId").description("오늘의 퀴즈 ID")
                                                                       .type(JsonFieldType.NUMBER),
                                fieldWithPath("answers[*].accountId").description("회원 ID")
                                                                     .type(JsonFieldType.NUMBER),
                                fieldWithPath("answers[*].todayQuizQuestion").description("오늘의 퀴즈 문제")
                                                                             .type(JsonFieldType.OBJECT),
                                fieldWithPath("answers[*].todayQuizQuestion.quizCategory").description("오늘의 퀴즈 문제 유형")
                                                                                          .type(JsonFieldType.STRING),
                                fieldWithPath("answers[*].todayQuizQuestion.question").description("오늘의 퀴즈 문제 내용")
                                                                                      .type(JsonFieldType.STRING),
                                fieldWithPath("answers[*].todayQuizQuestion.passage").description(
                                                                                                     "오늘의 퀴즈 문제 지문")
                                                                                             .type(JsonFieldType.STRING),
                                fieldWithPath("answers[*].selectedQuizOptionContent").description(
                                                                                             "해당 오늘의 퀴즈에서 회원이 제출한 답")
                                                                                     .type(JsonFieldType.STRING),
                                fieldWithPath("answers[*].answerQuizOptionContent").description("해당 오늘의 퀴즈 답")
                                                                                   .type(JsonFieldType.STRING),
                                fieldWithPath("answers[*].corrected").description("정답 여부")
                                                                     .type(JsonFieldType.BOOLEAN)
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 특정_오늘의_퀴즈에_대한_채점_결과_조회_요청_성공_테스트() throws Exception {
        // given
        TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizGradedAnswerResponse.TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"
        );
        TodayQuizGradedAnswerResponse todayQuizGradedAnswerResponse = new TodayQuizGradedAnswerResponse(
                1L,
                1L,
                1L,
                todayQuizQuestionResponse,
                "Authorization",
                "Authorization",
                true
        );

        given(todayQuizService.readTargetTodayQuizGradedAnswers(anyLong(), anyLong())).willReturn(
                todayQuizGradedAnswerResponse);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/today-quizzes/{todayQuizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION,
                        "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("id", is(1L), Long.class),
                jsonPath("todayQuizId", is(1L), Long.class),
                jsonPath("accountId", is(1L), Long.class),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.passage").value(
                        "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("selectedQuizOptionContent").value("Authorization"),
                jsonPath("answerQuizOptionContent").value("Authorization")
        );

        verify(todayQuizService).readTargetTodayQuizGradedAnswers(anyLong(), anyLong());

        특정_오늘의_퀴즈에_대한_채점_결과_조회_요청_문서화(resultActions);
    }

    private void 특정_오늘의_퀴즈에_대한_채점_결과_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("todayQuizId").description("오늘의 퀴즈 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("채점 ID")
                                                   .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizId").description("오늘의 퀴즈 ID")
                                                            .type(JsonFieldType.NUMBER),
                                fieldWithPath("accountId").description("회원 ID")
                                                          .type(JsonFieldType.NUMBER),
                                fieldWithPath("todayQuizQuestion").description("오늘의 퀴즈 문제")
                                                                  .type(JsonFieldType.OBJECT),
                                fieldWithPath("todayQuizQuestion.quizCategory").description("오늘의 퀴즈 문제 유형")
                                                                               .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.question").description("오늘의 퀴즈 문제")
                                                                           .type(JsonFieldType.STRING),
                                fieldWithPath("todayQuizQuestion.passage").description("오늘의 퀴즈 문제 지문")
                                                                                  .type(JsonFieldType.STRING),
                                fieldWithPath("selectedQuizOptionContent").description("해당 오늘의 퀴즈에서 회원이 제출한 답")
                                                                          .type(JsonFieldType.STRING),
                                fieldWithPath("answerQuizOptionContent").description("해당 오늘의 퀴즈 답")
                                                                        .type(JsonFieldType.STRING),
                                fieldWithPath("corrected").description("정답 여부")
                                                          .type(JsonFieldType.BOOLEAN)
                        )
                )
        );
    }
}
