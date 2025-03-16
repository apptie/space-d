package com.dnd.spaced.global.config;

import com.dnd.spaced.global.auth.interceptor.AuthInterceptor;
import com.dnd.spaced.global.auth.resolver.AuthAccountInfoArgumentResolver;
import com.dnd.spaced.global.auth.resolver.GuestAccountInfoArgumentResolver;
import com.dnd.spaced.global.log.QueryTraceInterceptor;
import com.dnd.spaced.global.resolver.admin.report.ReportPageableArgumentResolver;
import com.dnd.spaced.global.resolver.bookmark.BookmarkPageableArgumentResolver;
import com.dnd.spaced.global.resolver.comment.CommentPageableArgumentResolver;
import com.dnd.spaced.global.resolver.quiz.GradedAnswerPageableArgumentResolver;
import com.dnd.spaced.global.resolver.word.WordPageableArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final AuthInterceptor authInterceptor;
    private final QueryTraceInterceptor queryTraceInterceptor;
    private final AuthAccountInfoArgumentResolver authAccountInfoArgumentResolver;
    private final GuestAccountInfoArgumentResolver guestAccountInfoArgumentResolver;
    private final ReportPageableArgumentResolver reportPageableArgumentResolver;
    private final BookmarkPageableArgumentResolver bookmarkPageableArgumentResolver;
    private final CommentPageableArgumentResolver commentPageableArgumentResolver;
    private final GradedAnswerPageableArgumentResolver gradedAnswerPageableArgumentResolver;
    private final WordPageableArgumentResolver wordPageableArgumentResolver;

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.simpleDateFormat(DATE_TIME_FORMAT);
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        };
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authAccountInfoArgumentResolver);
        resolvers.add(guestAccountInfoArgumentResolver);
        resolvers.add(wordPageableArgumentResolver);
        resolvers.add(commentPageableArgumentResolver);
        resolvers.add(reportPageableArgumentResolver);
        resolvers.add(bookmarkPageableArgumentResolver);
        resolvers.add(gradedAnswerPageableArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**", "/words/**");
        registry.addInterceptor(queryTraceInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ResourceHttpMessageConverter converter = new ResourceHttpMessageConverter();
        converter.setSupportedMediaTypes(
                List.of(
                        MediaType.IMAGE_PNG,
                        MediaType.IMAGE_JPEG,
                        MediaType.IMAGE_GIF
                )
        );
    }
}
