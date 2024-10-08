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
                                      beneathPath("data.authProfileException").withSubsectionId("authProfileException"),
                                      attributes(key("title").value("`POST /auths/profile` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getAuthProfileException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.refreshTokenException").withSubsectionId("refreshTokenException"),
                                      attributes(key("title").value("`POST /auths/refresh-token` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getRefreshTokenException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.registerBlacklistTokenException").withSubsectionId("registerBlacklistTokenException"),
                                      attributes(key("title").value("`POST /auths/blacklist-token` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getRegisterBlacklistTokenException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.withdrawalException").withSubsectionId("withdrawalException"),
                                      attributes(key("title").value("`DELETE /accounts/withdrawal` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getWithdrawalException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.changeCareerInfoException").withSubsectionId("changeCareerInfoException"),
                                      attributes(key("title").value("`PUT /accounts/career-info` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getChangeCareerInfoException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.changeProfileInfoException").withSubsectionId("changeProfileInfoException"),
                                      attributes(key("title").value("`PUT /accounts/profile-info` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getChangeProfileInfoException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.findAccountInfoException").withSubsectionId("findAccountInfoException"),
                                      attributes(key("title").value("`GET /accounts` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getFindAccountInfoException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.saveWordException").withSubsectionId("saveWordException"),
                                      attributes(key("title").value("`POST /admin/words` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getSaveWordException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.updateWordExampleException").withSubsectionId("updateWordExampleException"),
                                      attributes(key("title").value("`PATCH /admin/words/examples/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getUpdateWordExampleException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.deleteWordExampleException").withSubsectionId("deleteWordExampleException"),
                                      attributes(key("title").value("`DELETE /admin/words/examples/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getDeleteWordExampleException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.deletePronunciationException").withSubsectionId("deletePronunciationException"),
                                      attributes(key("title").value("`DELETE /admin/words/pronunciations/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getDeletePronunciationException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.readWordException").withSubsectionId("readWordException"),
                                      attributes(key("title").value("`GET /words/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getReadWordException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.saveCommentException").withSubsectionId("saveCommentException"),
                                      attributes(key("title").value("`POST /words/{wordId}/comments` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getSaveCommentException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.deleteCommentException").withSubsectionId("deleteCommentException"),
                                      attributes(key("title").value("`DELETE /comments/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getDeleteCommentException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.updateCommentException").withSubsectionId("updateCommentException"),
                                      attributes(key("title").value("`PUT /comments/{id}` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getUpdateCommentException())
                              ),
                              customResponseFields(
                                      "exception-response",
                                      beneathPath("data.processLikeException").withSubsectionId("processLikeException"),
                                      attributes(key("title").value("`POST /comments/{commentId}/likes` 예외 상황")),
                                      exceptionConvertFieldDescriptor(data.getProcessLikeException())
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
