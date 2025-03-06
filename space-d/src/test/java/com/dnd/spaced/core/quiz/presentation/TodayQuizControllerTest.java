package com.dnd.spaced.core.quiz.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeTodayQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadTodayQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizGradedAnswerResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizQuestionResponse.TodayQuizOptionResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SuppressWarnings("NonAsciiCharacters")
class TodayQuizControllerTest extends CommonControllerSliceTest {

    @Test
    void 최신_오늘의_퀴즈_요청_성공_테스트() throws Exception {
        // given
        TodayQuizOptionResponse authorizationOption = new TodayQuizOptionResponse(1L, "Authorization");
        TodayQuizOptionResponse controllerOption = new TodayQuizOptionResponse(2L, "Controller");
        TodayQuizOptionResponse domainOption = new TodayQuizOptionResponse(3L, "Domain");
        TodayQuizOptionResponse repositoryOption = new TodayQuizOptionResponse(4L, "Repository");
        TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of(authorizationOption, controllerOption, domainOption, repositoryOption),
                1L
        );
        TodayQuizResponse todayQuizResponse = new TodayQuizResponse(1L, todayQuizQuestionResponse);

        given(todayQuizService.findLatest()).willReturn(todayQuizResponse);

        // when & then
        mockMvc.perform(
                get("/today-quizzes/latest").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").exists(),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.questionContent").value("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("todayQuizQuestion.todayQuizOptions").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].id").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].content").exists()
        );
    }

    @Test
    void 오늘의_퀴즈_조회_요청_성공_테스트() throws Exception {
        // given
        TodayQuizOptionResponse authorizationOption = new TodayQuizOptionResponse(1L, "Authorization");
        TodayQuizOptionResponse controllerOption = new TodayQuizOptionResponse(2L, "Controller");
        TodayQuizOptionResponse domainOption = new TodayQuizOptionResponse(3L, "Domain");
        TodayQuizOptionResponse repositoryOption = new TodayQuizOptionResponse(4L, "Repository");
        TodayQuizQuestionResponse todayQuizQuestionResponse = new TodayQuizQuestionResponse(
                "개발",
                "다음 예문을 보고 예문에 맞는 용어를 선택해주세요.",
                "인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                List.of(authorizationOption, controllerOption, domainOption, repositoryOption),
                1L
        );
        TodayQuizResponse todayQuizResponse = new TodayQuizResponse(1L, todayQuizQuestionResponse);

        given(todayQuizService.findBy(anyLong())).willReturn(todayQuizResponse);

        // when & then
        mockMvc.perform(
                get("/today-quizzes/{todayQuizId}", 1L).accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                jsonPath("id").exists(),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.questionContent").value("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("todayQuizQuestion.todayQuizOptions").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].id").exists(),
                jsonPath("todayQuizQuestion.todayQuizOptions[*].content").exists()
        );
    }

    @Test
    @WithMockUser("1")
    void 오늘의_퀴즈_채점_요청_성공_테스트() throws Exception {
        // given
        willDoNothing().given(todayQuizService).grade(anyLong(), anyLong(), any(GradeTodayQuizRequest.class));

        // when & then
        GradeTodayQuizRequest request = new GradeTodayQuizRequest(0);

        mockMvc.perform(
                post("/today-quizzes/{todayQuizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                                                                       .contentType(MediaType.APPLICATION_JSON)
                                                                       .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "/today-quizzes/1/graded-answers")
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
                todayQuizService.findTodayQuizGradedAnswerAllBy(
                        anyLong(),
                        any(ReadTodayQuizGradedAnswerSearchRequest.class),
                        any(Pageable.class)
                )
        ).willReturn(response);

        // when & then
        ReadTodayQuizGradedAnswerSearchRequest request = new ReadTodayQuizGradedAnswerSearchRequest(
                null
        );

        mockMvc.perform(
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
                jsonPath("answers[0].todayQuizQuestion.questionContent").value("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("answers[0].selectedQuizOptionContent").value("Authorization"),
                jsonPath("answers[0].answerQuizOptionContent").value("Authorization")
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

        given(todayQuizService.findTodayQuizGradedAnswerBy(anyLong(), anyLong())).willReturn(todayQuizGradedAnswerResponse);

        // when & then
        mockMvc.perform(
                get("/today-quizzes/{todayQuizId}/graded-answers", 1L).header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
        ).andExpectAll(
                status().isOk(),
                jsonPath("id", is(1L), Long.class),
                jsonPath("todayQuizId", is(1L), Long.class),
                jsonPath("accountId", is(1L), Long.class),
                jsonPath("todayQuizQuestion").exists(),
                jsonPath("todayQuizQuestion.quizCategory").value("개발"),
                jsonPath("todayQuizQuestion.question").value("다음 예문을 보고 예문에 맞는 용어를 선택해주세요."),
                jsonPath("todayQuizQuestion.questionContent").value("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                jsonPath("selectedQuizOptionContent").value("Authorization"),
                jsonPath("answerQuizOptionContent").value("Authorization")
        );
    }
}
