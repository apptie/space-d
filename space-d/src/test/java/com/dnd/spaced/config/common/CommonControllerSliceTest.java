package com.dnd.spaced.config.common;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.dnd.spaced.config.context.RestControllerTestInitializer;
import com.dnd.spaced.config.docs.RestDocsConfiguration;
import com.dnd.spaced.config.listener.ResetMockTestExecutionListener;
import com.dnd.spaced.config.stub.StudAccountRepository;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.auth.resolver.GuestAccountInfoArgumentResolver;
import com.dnd.spaced.global.exception.GlobalControllerAdvice;
import com.dnd.spaced.global.resolver.admin.report.ReportPageableArgumentResolver;
import com.dnd.spaced.global.resolver.bookmark.BookmarkPageableArgumentResolver;
import com.dnd.spaced.global.resolver.comment.CommentPageableArgumentResolver;
import com.dnd.spaced.global.resolver.quiz.GradedAnswerPageableArgumentResolver;
import com.dnd.spaced.global.resolver.quiz.QuizPageableArgumentResolver;
import com.dnd.spaced.global.resolver.word.WordPageableArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CharacterEncodingFilter;

@Import(RestDocsConfiguration.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = RestControllerTestInitializer.class)
@TestExecutionListeners(value = ResetMockTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CommonControllerSliceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @Autowired
    ApplicationContext applicationContext;

    protected MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(findRestControllers());
        this.mockMvc = new FixedStandaloneMockMvcBuilder(standaloneMockMvcBuilder).configureMessageConverters()
                                                                                  .configureArgumentResolvers()
                                                                                  .configureInterceptors()
                                                                                  .configureControllerAdvice()
                                                                                  .configureRestDocs()
                                                                                  .configureFilters()
                                                                                  .build();
    }

    private Object[] findRestControllers() {
        return applicationContext.getBeansWithAnnotation(RestController.class)
                                 .values()
                                 .toArray();
    }

    private class FixedStandaloneMockMvcBuilder {

        final StandaloneMockMvcBuilder builder;
        final AuthStore store;

        public FixedStandaloneMockMvcBuilder(StandaloneMockMvcBuilder builder) {
            this.builder = builder;
            this.store = new AuthStore();
        }

        MockMvc build() {
            return builder.build();
        }

        FixedStandaloneMockMvcBuilder configureMessageConverters() {
            MappingJackson2HttpMessageConverter jacksonMessageConverter =
                    new MappingJackson2HttpMessageConverter(objectMapper);
            ResourceHttpMessageConverter resourceMessageConverter = new ResourceHttpMessageConverter();
            resourceMessageConverter.setSupportedMediaTypes(
                    List.of(
                            MediaType.IMAGE_PNG,
                            MediaType.IMAGE_JPEG,
                            MediaType.IMAGE_GIF
                    )
            );

            builder.setMessageConverters(jacksonMessageConverter, resourceMessageConverter);
            return this;
        }

        FixedStandaloneMockMvcBuilder configureArgumentResolvers() {
            builder.setCustomArgumentResolvers(
                    new AuthAccountInfoArgumentResolver(store, new StudAccountRepository()),
                    new GuestAccountInfoArgumentResolver(store),
                    new WordPageableArgumentResolver(),
                    new CommentPageableArgumentResolver(),
                    new GradedAnswerPageableArgumentResolver(),
                    new BookmarkPageableArgumentResolver(),
                    new ReportPageableArgumentResolver(),
                    new QuizPageableArgumentResolver()
            );
            return this;
        }

        FixedStandaloneMockMvcBuilder configureInterceptors() {
            builder.addInterceptors(new AuthInterceptor(store));
            return this;
        }

        FixedStandaloneMockMvcBuilder configureControllerAdvice() {
            builder.setControllerAdvice(new GlobalControllerAdvice());
            return this;
        }

        FixedStandaloneMockMvcBuilder configureRestDocs() {
            builder.apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                   .alwaysDo(print())
                   .alwaysDo(restDocs);
            return this;
        }

        FixedStandaloneMockMvcBuilder configureFilters() {
            builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
            return this;
        }
    }
}
