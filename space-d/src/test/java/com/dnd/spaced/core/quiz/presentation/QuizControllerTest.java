package com.dnd.spaced.core.quiz.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest.SubmitAnswerRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse.GradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse.GradedAnswerResponse.QuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@SuppressWarnings("NonAsciiCharacters")
class QuizControllerTest extends CommonControllerSliceTest {

    @Test
    @WithMockUser("1")
    void 퀴즈_생성_요청_성공_테스트() throws Exception {
        // given
        given(quizService.createQuiz(anyLong(), any(CreateQuizRequest.class))).willReturn(1L);

        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/quizzes").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/quizzes/1")
        );

        verify(quizService).createQuiz(anyLong(), any(CreateQuizRequest.class));

        퀴즈_생성_요청_문서화(resultActions);
    }

    private void 퀴즈_생성_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 퀴즈_채점_요청_성공_테스트() throws Exception {
        // given
        willDoNothing().given(quizService).grade(anyLong(), anyLong(), any(GradeQuizRequest.class));

        SubmitAnswerRequest[] submitAnswers = {
                new SubmitAnswerRequest(1L, "Authorization"),
                new SubmitAnswerRequest(2L, "Domain"),
                new SubmitAnswerRequest(3L, "Controller"),
                new SubmitAnswerRequest(2L, "Web"),
                new SubmitAnswerRequest(1L, "HTTP")
        };
        GradeQuizRequest request = new GradeQuizRequest(submitAnswers);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                post("/quizzes/{quizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/quizzes/1/graded-answer")
        );

        verify(quizService).grade(anyLong(), anyLong(), any(GradeQuizRequest.class));

        퀴즈_채점_요청_문서화(resultActions);
    }

    private void 퀴즈_채점_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("quizId").description("퀴즈 ID")
                        ),
                        requestFields(
                                fieldWithPath("submitAnswers").type(JsonFieldType.ARRAY).description("퀴즈 문제 정답"),
                                fieldWithPath("submitAnswers[*].wordId").type(JsonFieldType.NUMBER).description("정답 용어 ID"),
                                fieldWithPath("submitAnswers[*].content").type(JsonFieldType.STRING).description("정답 용어 이름")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 회원이_제출한_퀴즈에_대한_채점_결과_목록_조회_요청_성공_테스트() throws Exception {
        // given
        QuizQuestionResponse quizQuestionResponse1 = new QuizQuestionResponse(
                1L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"

        );
        GradedAnswerResponse gradedAnswerResponse1 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse1,
                "Authorization",
                "Authorization",
                true
        );
        QuizQuestionResponse quizQuestionResponse2 = new QuizQuestionResponse(
                2L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다."

        );
        GradedAnswerResponse gradedAnswerResponse2 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse2,
                "YAML",
                "YAML",
                true
        );
        QuizQuestionResponse quizQuestionResponse3 = new QuizQuestionResponse(
                3L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다."

        );
        GradedAnswerResponse gradedAnswerResponse3 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse3,
                "TOML",
                "XML",
                false
        );
        QuizQuestionResponse quizQuestionResponse4 = new QuizQuestionResponse(
                4L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다."

        );
        GradedAnswerResponse gradedAnswerResponse4 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse4,
                "deprecated",
                "deprecated",
                true
        );
        QuizQuestionResponse quizQuestionResponse5 = new QuizQuestionResponse(
                5L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "개발에서는 주로 프로그램이나 코드, 명령을 실행할 때 사용됩니다."

        );
        GradedAnswerResponse gradedAnswerResponse5 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse5,
                "execute",
                "execute",
                true
        );
        GradedAnswerCollectionResponse response = new GradedAnswerCollectionResponse(
                List.of(
                        gradedAnswerResponse1,
                        gradedAnswerResponse2,
                        gradedAnswerResponse3,
                        gradedAnswerResponse4,
                        gradedAnswerResponse5
                ),
                gradedAnswerResponse5.id()
        );
        given(quizService.readGradedAnswers(anyLong(), any(ReadQuizGradedAnswerSearchRequest.class), any(Pageable.class))).willReturn(response);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/quizzes/graded-answers").header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                              .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("answers").exists(),
                jsonPath("answers.size()").value(5),
                jsonPath("answers[*].accountId").exists(),
                jsonPath("answers[*].quizId").exists(),
                jsonPath("answers[*].quizQuestion").exists(),
                jsonPath("answers[*].quizQuestion.id").exists(),
                jsonPath("answers[*].quizQuestion.quizCategory").exists(),
                jsonPath("answers[*].quizQuestion.question").exists(),
                jsonPath("answers[*].quizQuestion.questionContent").exists(),
                jsonPath("answers[*].selectedQuizOptionContent").exists(),
                jsonPath("answers[*].answerQuizOptionContent").exists(),
                jsonPath("answers[*].isCorrect").exists()
        );

        verify(quizService).readGradedAnswers(
                anyLong(),
                any(ReadQuizGradedAnswerSearchRequest.class),
                any(Pageable.class)
        );

        회원이_제출한_퀴즈에_대한_채점_결과_목록_조회_요청_문서화(resultActions);
    }

    private void 회원이_제출한_퀴즈에_대한_채점_결과_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        queryParameters(
                                parameterWithName("lastQuizGradedAnswerId").description("마지막으로 조회한 회원 제출 답 ID")
                                                                                   .optional()
                        ),
                        responseFields(
                                fieldWithPath("answers").type(JsonFieldType.ARRAY).description("회원이 제출한 답 목록"),
                                fieldWithPath("answers[*].id").type(JsonFieldType.NUMBER).description("회원이 제출한 답 목록"),
                                fieldWithPath("answers[*].accountId").type(JsonFieldType.NUMBER).description("퀴즈 생성 회원 ID"),
                                fieldWithPath("answers[*].quizId").type(JsonFieldType.NUMBER).description("퀴즈 ID"),
                                fieldWithPath("answers[*].quizQuestion").type(JsonFieldType.OBJECT).description("퀴즈 문제"),
                                fieldWithPath("answers[*].quizQuestion.id").type(JsonFieldType.NUMBER).description("퀴즈 문제 ID"),
                                fieldWithPath("answers[*].quizQuestion.quizCategory").type(JsonFieldType.STRING).description("퀴즈 문제 카테고리"),
                                fieldWithPath("answers[*].quizQuestion.question").type(JsonFieldType.STRING).description("퀴즈 문제 내용"),
                                fieldWithPath("answers[*].quizQuestion.questionContent").type(JsonFieldType.STRING).description("퀴즈 문제 지문"),
                                fieldWithPath("answers[*].selectedQuizOptionContent").type(JsonFieldType.STRING).description("퀴즈 문제 회원 제출 답 내용"),
                                fieldWithPath("answers[*].answerQuizOptionContent").type(JsonFieldType.STRING).description("퀴즈 문제 정답 내용"),
                                fieldWithPath("answers[*].isCorrect").type(JsonFieldType.BOOLEAN).description("정답 여부"),
                                fieldWithPath("lastGradedAnswerId").type(JsonFieldType.NUMBER).description("마지막으로 조회한 퀴즈 제출 답 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 특정_퀴즈에_대한_회원이_제출한_답_목록_조회_요청_성공_테스트() throws Exception {
        // given
        QuizQuestionResponse quizQuestionResponse1 = new QuizQuestionResponse(
                1L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"

        );
        GradedAnswerResponse gradedAnswerResponse1 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse1,
                "Authorization",
                "Authorization",
                true
        );
        QuizQuestionResponse quizQuestionResponse2 = new QuizQuestionResponse(
                2L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다."

        );
        GradedAnswerResponse gradedAnswerResponse2 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse2,
                "YAML",
                "YAML",
                true
        );
        QuizQuestionResponse quizQuestionResponse3 = new QuizQuestionResponse(
                3L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다."

        );
        GradedAnswerResponse gradedAnswerResponse3 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse3,
                "TOML",
                "XML",
                false
        );
        QuizQuestionResponse quizQuestionResponse4 = new QuizQuestionResponse(
                4L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다."

        );
        GradedAnswerResponse gradedAnswerResponse4 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse4,
                "deprecated",
                "deprecated",
                true
        );
        QuizQuestionResponse quizQuestionResponse5 = new QuizQuestionResponse(
                5L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "개발에서는 주로 프로그램이나 코드, 명령을 실행할 때 사용됩니다."

        );
        GradedAnswerResponse gradedAnswerResponse5 = new GradedAnswerResponse(
                1L,
                1L,
                1L,
                quizQuestionResponse5,
                "execute",
                "execute",
                true
        );
        GradedAnswerCollectionResponse response = new GradedAnswerCollectionResponse(
                List.of(
                        gradedAnswerResponse1,
                        gradedAnswerResponse2,
                        gradedAnswerResponse3,
                        gradedAnswerResponse4,
                        gradedAnswerResponse5
                ),
                gradedAnswerResponse5.id()
        );

        given(quizService.readGradedAnswers(anyLong(), anyLong())).willReturn(response);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/quizzes/{quizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                           .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("answers").exists(),
                jsonPath("answers.size()").value(5),
                jsonPath("answers[*].accountId").exists(),
                jsonPath("answers[*].quizId").exists(),
                jsonPath("answers[*].quizQuestion").exists(),
                jsonPath("answers[*].quizQuestion.id").exists(),
                jsonPath("answers[*].quizQuestion.quizCategory").exists(),
                jsonPath("answers[*].quizQuestion.question").exists(),
                jsonPath("answers[*].quizQuestion.questionContent").exists(),
                jsonPath("answers[*].selectedQuizOptionContent").exists(),
                jsonPath("answers[*].answerQuizOptionContent").exists(),
                jsonPath("answers[*].isCorrect").exists()
        );

        verify(quizService).readGradedAnswers(anyLong(), anyLong());

        특정_퀴즈에_대한_회원이_제출한_답_목록_조회_요청_문서화(resultActions);
    }

    private void 특정_퀴즈에_대한_회원이_제출한_답_목록_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("quizId").description("퀴즈 ID")
                        ),
                        responseFields(
                                fieldWithPath("answers").type(JsonFieldType.ARRAY).description("회원이 제출한 답 목록"),
                                fieldWithPath("answers[*].id").type(JsonFieldType.NUMBER).description("회원이 제출한 답 목록"),
                                fieldWithPath("answers[*].accountId").type(JsonFieldType.NUMBER).description("퀴즈 생성 회원 ID"),
                                fieldWithPath("answers[*].quizId").type(JsonFieldType.NUMBER).description("퀴즈 ID"),
                                fieldWithPath("answers[*].quizQuestion").type(JsonFieldType.OBJECT).description("퀴즈 문제"),
                                fieldWithPath("answers[*].quizQuestion.id").type(JsonFieldType.NUMBER).description("퀴즈 문제 ID"),
                                fieldWithPath("answers[*].quizQuestion.quizCategory").type(JsonFieldType.STRING).description("퀴즈 문제 카테고리"),
                                fieldWithPath("answers[*].quizQuestion.question").type(JsonFieldType.STRING).description("퀴즈 문제 내용"),
                                fieldWithPath("answers[*].quizQuestion.questionContent").type(JsonFieldType.STRING).description("퀴즈 문제 지문"),
                                fieldWithPath("answers[*].selectedQuizOptionContent").type(JsonFieldType.STRING).description("퀴즈 문제 회원 제출 답 내용"),
                                fieldWithPath("answers[*].answerQuizOptionContent").type(JsonFieldType.STRING).description("퀴즈 문제 정답 내용"),
                                fieldWithPath("answers[*].isCorrect").type(JsonFieldType.BOOLEAN).description("정답 여부"),
                                fieldWithPath("lastGradedAnswerId").type(JsonFieldType.NUMBER).description("마지막으로 조회한 퀴즈 제출 답 ID")
                        )
                )
        );
    }

    @Test
    @WithMockUser("1")
    void 퀴즈_조회_요청_성공_테스트() throws Exception {
        // given
        given(quizService.readQuiz(anyLong(), anyLong())).willReturn(createQuizResponse());

        // when & then
        ResultActions resultActions = mockMvc.perform(
                get("/quizzes/{quizId}", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("id", is(1L), Long.class),
                jsonPath("accountId", is(1L), Long.class),
                jsonPath("quizQuestions").exists(),
                jsonPath("quizQuestions[0].id", is(1L), Long.class),
                jsonPath("quizQuestions[0].quizCategory", is("개발")),
                jsonPath("quizQuestions[0].question", is("다음 예문을 보고 예문에 맞는 용어를 선택해주세요.")),
                jsonPath("quizQuestions[0].questionContent", is("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")),
                jsonPath("quizQuestions[0].quizOptions").exists(),
                jsonPath("quizQuestions[0].quizOptions[0].id", is(1L), Long.class),
                jsonPath("quizQuestions[0].quizOptions[0].content", is("Authorization")),
                jsonPath("quizQuestions[0].quizOptions[1].id", is(2L), Long.class),
                jsonPath("quizQuestions[0].quizOptions[1].content", is("status")),
                jsonPath("quizQuestions[0].quizOptions[2].id", is(3L), Long.class),
                jsonPath("quizQuestions[0].quizOptions[2].content", is("gradient")),
                jsonPath("quizQuestions[0].quizOptions[3].id", is(4L), Long.class),
                jsonPath("quizQuestions[0].quizOptions[3].content", is("locale")),
                jsonPath("quizQuestions[0].answerOptionWordId", is(1L), Long.class)
        );

        verify(quizService).readQuiz(anyLong(), anyLong());

        퀴즈_조회_요청_문서화(resultActions);
    }

    private void 퀴즈_조회_요청_문서화(ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 타입의 Access Token")
                        ),
                        pathParameters(
                                parameterWithName("quizId").description("퀴즈 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("퀴즈 ID"),
                                fieldWithPath("accountId").type(JsonFieldType.NUMBER).description("퀴즈 생성 퀴즈 생성 회원 ID"),
                                fieldWithPath("quizQuestions").type(JsonFieldType.ARRAY).description("퀴즈 문제 목록"),
                                fieldWithPath("quizQuestions[*].id").type(JsonFieldType.NUMBER).description("퀴즈 문제 ID"),
                                fieldWithPath("quizQuestions[*].quizCategory").type(JsonFieldType.STRING).description("퀴즈 문제 카테고리"),
                                fieldWithPath("quizQuestions[*].question").type(JsonFieldType.STRING).description("퀴즈 문제 내용"),
                                fieldWithPath("quizQuestions[*].questionContent").type(JsonFieldType.STRING).description("퀴즈 문제 지문"),
                                fieldWithPath("quizQuestions[*].quizOptions").type(JsonFieldType.ARRAY).description("퀴즈 문제 보기"),
                                fieldWithPath("quizQuestions[*].quizOptions[*].id").type(JsonFieldType.NUMBER).description("퀴즈 문제 보기 ID"),
                                fieldWithPath("quizQuestions[*].quizOptions[*].content").type(JsonFieldType.STRING).description("퀴즈 문제 보기 내용"),
                                fieldWithPath("quizQuestions[*].answerOptionWordId").type(JsonFieldType.NUMBER).description("퀴즈 문제 정답 용어 ID")
                        )
                )
        );
    }

    private QuizResponse createQuizResponse() {
        QuizResponse.QuizQuestionResponse quizQuestionResponse1 = new QuizResponse.QuizQuestionResponse(
                1L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of(
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(1L, "Authorization"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(2L, "status"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(3L, "gradient"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(4L, "locale")
                ),
                1L
        );
        QuizResponse.QuizQuestionResponse quizQuestionResponse2 = new QuizResponse.QuizQuestionResponse(
                2L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다.",
                List.of(
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(5L, "YAML"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(6L, "jar"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(7L, "redirect"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(8L, "empty")
                ),
                2L
        );
        QuizResponse.QuizQuestionResponse quizQuestionResponse3 = new QuizResponse.QuizQuestionResponse(
                3L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다.",
                List.of(
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(9L, "TOML"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(10L, "directory"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(11L, "SaaS"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(12L, "usage")
                ),
                3L
        );
        QuizResponse.QuizQuestionResponse quizQuestionResponse4 = new QuizResponse.QuizQuestionResponse(
                4L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다.",
                List.of(
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(13L, "deprecated"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(14L, "GUI"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(15L, "JWT"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(16L, "Dequeue")
                ),
                4L
        );
        QuizResponse.QuizQuestionResponse quizQuestionResponse5 = new QuizResponse.QuizQuestionResponse(
                5L,
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "개발에서는 주로 프로그램이나 코드, 명령을 실행할 때 사용됩니다.",
                List.of(
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(17L, "execute"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(18L, "COALESCE"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(19L, "Queue"),
                        new QuizResponse.QuizQuestionResponse.QuizOptionResponse(20L, "carousel")
                ),
                5L
        );
        return new QuizResponse(
                1L,
                1L,
                List.of(quizQuestionResponse1, quizQuestionResponse2, quizQuestionResponse3, quizQuestionResponse4, quizQuestionResponse5)
        );
    }
}
