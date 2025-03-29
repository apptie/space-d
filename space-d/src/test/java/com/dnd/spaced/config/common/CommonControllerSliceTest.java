package com.dnd.spaced.config.common;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.dnd.spaced.config.docs.RestDocsConfiguration;
import com.dnd.spaced.config.docs.snippet.enums.EnumDocsController;
import com.dnd.spaced.config.docs.snippet.exceptions.account.AccountExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.admin.AdminExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.auth.AuthExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.bookmark.BookmarkExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.comment.CommentExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.like.LikeExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.localimage.LocalImageExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.quiz.QuizExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.report.ReportExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.skill.SkillExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.todayquiz.TodayQuizExceptionController;
import com.dnd.spaced.config.docs.snippet.exceptions.word.WordExceptionController;
import com.dnd.spaced.config.listener.ResetMockTestExecutionListener;
import com.dnd.spaced.config.processor.InjectMockBeanFactoryPostProcessor;
import com.dnd.spaced.config.stub.StudAccountRepository;
import com.dnd.spaced.core.account.presentation.AccountController;
import com.dnd.spaced.core.admin.presentation.AdminAuthenticationController;
import com.dnd.spaced.core.admin.presentation.AdminReportController;
import com.dnd.spaced.core.admin.presentation.AdminTodayQuizController;
import com.dnd.spaced.core.admin.presentation.AdminWordController;
import com.dnd.spaced.core.auth.presentation.AuthController;
import com.dnd.spaced.core.bookmark.presentation.BookmarkController;
import com.dnd.spaced.core.comment.presentation.CommentController;
import com.dnd.spaced.core.image.presentation.LocalImageController;
import com.dnd.spaced.core.like.presentation.LikeController;
import com.dnd.spaced.core.quiz.presentation.QuizController;
import com.dnd.spaced.core.quiz.presentation.TodayQuizController;
import com.dnd.spaced.core.report.presentation.ReportController;
import com.dnd.spaced.core.skill.presentation.SkillController;
import com.dnd.spaced.core.word.presentation.WordController;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.auth.resolver.GuestAccountInfoArgumentResolver;
import com.dnd.spaced.global.exception.GlobalControllerAdvice;
import com.dnd.spaced.global.log.QueryTraceInterceptor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = {
                EnumDocsController.class, AccountExceptionController.class, AuthExceptionController.class,
                AdminExceptionController.class, WordExceptionController.class, ReportExceptionController.class,
                CommentExceptionController.class, LikeExceptionController.class, QuizExceptionController.class,
                TodayQuizExceptionController.class, LocalImageExceptionController.class,
                BookmarkExceptionController.class,
                SkillExceptionController.class,
                AuthController.class, AdminReportController.class, AccountController.class,
                WordController.class, CommentController.class, LikeController.class, QuizController.class,
                TodayQuizController.class, LocalImageController.class, ReportController.class,
                BookmarkController.class, SkillController.class, AdminAuthenticationController.class,
                AdminWordController.class, AdminTodayQuizController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = QueryTraceInterceptor.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthAccountInfoArgumentResolver.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GuestAccountInfoArgumentResolver.class)
        }
)
@Import({
        RestDocsConfiguration.class,
        InjectMockBeanFactoryPostProcessor.class
})
@TestExecutionListeners(value = ResetMockTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CommonControllerSliceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @Autowired
    WebApplicationContext webApplicationContext;

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
        return webApplicationContext.getBeansWithAnnotation(RestController.class)
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
