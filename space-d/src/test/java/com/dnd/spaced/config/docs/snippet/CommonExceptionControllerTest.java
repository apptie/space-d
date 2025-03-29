package com.dnd.spaced.config.docs.snippet;

import static com.dnd.spaced.config.docs.RestDocsConfiguration.field;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.dnd.spaced.config.common.CommonControllerSliceTest;
import com.dnd.spaced.config.docs.CustomResponseFieldsSnippet;
import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.config.docs.snippet.exceptions.ExceptionContent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;

public abstract class CommonExceptionControllerTest extends CommonControllerSliceTest {

    protected CustomResponseFieldsSnippet customResponseFields(
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

    protected FieldDescriptor[] exceptionConvertFieldDescriptor(Map<String, ExceptionContent> exceptionValues) {
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

    protected <T> T findExceptionData(MvcResult result, Class<T> responseType) throws IOException {
        CommonDocsResponse<T> apiResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                objectMapper.getTypeFactory().constructParametricType(CommonDocsResponse.class, responseType)
        );

        return apiResponseDto.data();
    }
}
