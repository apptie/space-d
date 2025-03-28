package com.dnd.spaced.config.docs.snippet;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.CustomResponseFieldsSnippet;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocs;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionDocs;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class DocsControllerTest extends CommonControllerSliceTest {

    @Test
    void enums() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/test/enums").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        EnumDocs data = findEnumData(mvcResult);

        result.andExpect(status().isOk())
              .andDo(
                      restDocs.document(
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.company").withSubsectionId("company"),
                                      attributes(key("title").value("Company 허용 값")),
                                      enumConvertFieldDescriptor(data.getCompany())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.jobGroup").withSubsectionId("jobGroup"),
                                      attributes(key("title").value("JobGroup 허용 값")),
                                      enumConvertFieldDescriptor(data.getJobGroup())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.experience").withSubsectionId("experience"),
                                      attributes(key("title").value("Experience 허용 값")),
                                      enumConvertFieldDescriptor(data.getExperience())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.category").withSubsectionId("category"),
                                      attributes(key("title").value("Category 허용 값")),
                                      enumConvertFieldDescriptor(data.getCategory())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.profileImageName").withSubsectionId("profileImageName"),
                                      attributes(key("title").value("ProfileImageName 허용 값")),
                                      enumConvertFieldDescriptor(data.getProfileImageName())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.pronunciationType").withSubsectionId("pronunciationType"),
                                      attributes(key("title").value("PronunciationType 허용 값")),
                                      enumConvertFieldDescriptor(data.getPronunciationType())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.quizCategory").withSubsectionId("quizCategory"),
                                      attributes(key("title").value("QuizCategory 허용 값")),
                                      enumConvertFieldDescriptor(data.getQuizCategory())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.reportReason").withSubsectionId("reportReason"),
                                      attributes(key("title").value("ReportReason 허용 값")),
                                      enumConvertFieldDescriptor(data.getReportReason())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.reportStatus").withSubsectionId("reportStatus"),
                                      attributes(key("title").value("ReportStatus 허용 값")),
                                      enumConvertFieldDescriptor(data.getReportStatus())
                              ),
                              customResponseFields(
                                      "enum-response",
                                      beneathPath("data.todayQuizStatus").withSubsectionId("todayQuizStatus"),
                                      attributes(key("title").value("TodayQuizStatus 허용 값")),
                                      enumConvertFieldDescriptor(data.getTodayQuizStatus())
                              )
                      ));
    }

    @Test
    void exceptions() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/test/exceptions").contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        ExceptionDocs data = findExceptionData(mvcResult);

        result.andExpect(status().isOk())
              .andDo(
                      restDocs.document(
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.createQuizException").withSubsectionId("createQuizException"),
                                      attributes(key("title").value("`POST /quizzes` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getCreateQuizException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.gradeQuizException").withSubsectionId("gradeQuizException"),
                                      attributes(key("title").value("`POST /quizzes/{quizId}/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getGradeQuizException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findGradedAnswersAllByException").withSubsectionId("findGradedAnswersAllByException"),
                                      attributes(key("title").value("`GET /quizzes/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindGradedAnswersAllByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findGradedAnswersAllByQuizException").withSubsectionId("findGradedAnswersAllByQuizException"),
                                      attributes(key("title").value("`GET /quizzes/{quizId}/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindGradedAnswersAllByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findQuizByException").withSubsectionId("findQuizByException"),
                                      attributes(key("title").value("`GET /quizzes/{quizId}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindQuizByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findLatestTodayQuizException").withSubsectionId("findLatestTodayQuizException"),
                                      attributes(key("title").value("`GET /today-quizzes/latest` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindLatestTodayQuizException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findTodayQuizByException").withSubsectionId("findTodayQuizByException"),
                                      attributes(key("title").value("`GET /today-quizzes/{todayQuizId}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindTodayQuizByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.gradeTodayQuizException").withSubsectionId("gradeTodayQuizException"),
                                      attributes(key("title").value("`POST /today-quizzes/{todayQuizId}/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getGradeTodayQuizException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findTodayQuizGradedAnswerByException").withSubsectionId("findTodayQuizGradedAnswerByException"),
                                      attributes(key("title").value("`GET /today-quizzes/{todayQuizId}/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindTodayQuizGradedAnswerByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findTodayQuizGradedAnswersAllByException").withSubsectionId("findTodayQuizGradedAnswersAllByException"),
                                      attributes(key("title").value("`GET /today-quizzes/graded-answers` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindTodayQuizGradedAnswersAllByException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.readLocalImageException").withSubsectionId("readLocalImageException"),
                                      attributes(key("title").value("`GET /images/{imageName}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getReadLocalImageException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.createBookmarkException").withSubsectionId("createBookmarkException"),
                                      attributes(key("title").value("`POST /bookmarks` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getCreateBookmarkException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.deleteBookmarkException").withSubsectionId("deleteBookmarkException"),
                                      attributes(key("title").value("`DELETE /bookmarks/{bookmarkId}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getDeleteBookmarkException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findAllBookmarkException").withSubsectionId("findAllBookmarkException"),
                                      attributes(key("title").value("`DELETE /bookmarks/{bookmarkId}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindAllBookmarkException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findSkillException").withSubsectionId("findSkillException"),
                                      attributes(key("title").value("`GET /skills` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindSkillException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.readQuizzesException").withSubsectionId("readQuizzesException"),
                                      attributes(key("title").value("`GET /quizzes` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getReadQuizzesException())
                              )
                      )
              );
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            PayloadSubsectionExtractor<?> subsectionExtractor,
            Map<String, Object> attributes,
            FieldDescriptor... descriptors
    ) {
        return new CustomResponseFieldsSnippet(
                type,
                subsectionExtractor,
                Arrays.asList(descriptors),
                attributes,
                true
        );
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet()
                         .stream()
                         .map(enumValue -> fieldWithPath(enumValue.getKey()).description(enumValue.getValue()))
                         .toArray(FieldDescriptor[]::new);
    }

    private EnumDocs findEnumData(MvcResult result) throws IOException {
        CommonDocsResponse<EnumDocs> apiResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        return apiResponseDto.data();
    }

    private static FieldDescriptor[] exceptionConvertFieldDescriptor(Map<String, ExceptionContent> exceptionValues) {
        return exceptionValues.entrySet()
                              .stream()
                              .map(
                                      exceptionValue -> fieldWithPath(exceptionValue.getKey()).description(exceptionValue.getValue().httpStatus().name())
                                                                                              .attributes(
                                                                                                      field("status", String.valueOf(exceptionValue.getValue().httpStatus().value())),
                                                                                                      field("message", exceptionValue.getValue().message())
                                                                                              )
                              ).toArray(FieldDescriptor[]::new);
    }

    private ExceptionDocs findExceptionData(MvcResult result) throws IOException {
        CommonDocsResponse<ExceptionDocs> apiResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                }
        );

        return apiResponseDto.data();
    }
}
