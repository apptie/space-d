package com.dnd.spaced.global.config;

import com.dnd.spaced.core.auth.application.BlacklistTokenService;
import com.dnd.spaced.core.auth.application.GenerateTokenService;
import com.dnd.spaced.core.auth.application.LoginService;
import com.dnd.spaced.core.auth.domain.TokenDecoder;
import com.dnd.spaced.global.auth.security.core.OAuth2UserDetailsService;
import com.dnd.spaced.global.auth.security.filter.OAuth2AuthenticationFilter;
import com.dnd.spaced.global.auth.security.filter.OAuth2RegistrationValidateFilter;
import com.dnd.spaced.global.auth.security.handler.OAuth2AccessDeniedHandler;
import com.dnd.spaced.global.auth.security.handler.OAuth2AuthenticationEntryPoint;
import com.dnd.spaced.global.auth.security.handler.OAuth2AuthenticationFailureHandler;
import com.dnd.spaced.global.auth.security.handler.OAuth2SuccessHandler;
import com.dnd.spaced.global.config.properties.CorsProperties;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenValidator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.converter.ClaimTypeConverter;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(
        value = {TokenProperties.class, CorsProperties.class, NicknameProperties.class}
)
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final TokenDecoder tokenDecoder;
    private final CorsProperties corsProperties;
    private final TokenProperties tokenProperties;
    private final RedisConnectionFactory redisConnectionFactory;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final LoginService loginService;
    private final GenerateTokenService generateTokenService;
    private final BlacklistTokenService blacklistTokenService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring()
                                         .requestMatchers(
                                                 PathRequest.toStaticResources()
                                                            .atCommonLocations()
                                         );
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/auths/refresh-token").permitAll()
                    .requestMatchers(HttpMethod.GET, "/words/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/words/{wordId}/comments").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(handler -> handler
                    .authenticationEntryPoint(oAuth2AuthenticationEntryPoint())
                    .accessDeniedHandler(oAuth2AccessDeniedHandler())
            )
            .oauth2Login(oauth -> oauth
                    .authorizationEndpoint(endPoint -> endPoint.baseUri("/login"))
                    .successHandler(oAuth2SuccessHandler())
                    .failureHandler(oAuth2AuthenticationFailureHandler())
            )
            .addFilterBefore(oAuth2AuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
            .addFilterBefore(oAuth2RegistrationValidateFilter(), OAuth2AuthorizationRequestRedirectFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(corsProperties.allowedOrigin());
        config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setExposedHeaders(
                List.of(
                        "Authorization",
                        "Origin",
                        "Accept",
                        "Access-Control-Allow-Headers",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "Content-Type"
                )
        );
        config.setAllowCredentials(true);
        config.setMaxAge(corsProperties.maxAge());

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        return new OAuth2AuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler(objectMapper);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(objectMapper, tokenProperties, loginService, generateTokenService);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public OAuth2AuthenticationFilter oAuth2AuthenticationFilter() {
        return new OAuth2AuthenticationFilter(oAuth2UserDetailsService());
    }

    @Bean
    public OAuth2UserDetailsService oAuth2UserDetailsService() {
        return new OAuth2UserDetailsService(tokenDecoder, blacklistTokenService);
    }

    @Bean
    public OAuth2RegistrationValidateFilter oAuth2RegistrationValidateFilter() {
        return new OAuth2RegistrationValidateFilter(objectMapper, handlerExceptionResolver);
    }

    @Bean
    public JwtDecoderFactory<ClientRegistration> jwtDecoderFactory() {
        return client -> {
            String jwkSetUri = client.getProviderDetails()
                                     .getJwkSetUri();
            NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                                                       .cache(
                                                               Objects.requireNonNull(
                                                                       oidcCacheManager().getCache("oidc::publicKey")
                                                               )
                                                       )
                                                       .build();

            decoder.setJwtValidator(
                    new DelegatingOAuth2TokenValidator<>(
                            JwtValidators.createDefault(), new OidcIdTokenValidator(client)
                    )
            );
            decoder.setClaimSetConverter(
                    new ClaimTypeConverter(OidcIdTokenDecoderFactory.createDefaultClaimTypeConverters())
            );
            return decoder;
        };
    }

    @Bean
    public CacheManager oidcCacheManager() {
        RedisCacheConfiguration redisCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                                       .serializeKeysWith(
                                               RedisSerializationContext.SerializationPair.fromSerializer(
                                                       new StringRedisSerializer()
                                               )
                                       )
                                       .serializeValuesWith(
                                               RedisSerializationContext.SerializationPair.fromSerializer(
                                                       new GenericJackson2JsonRedisSerializer()
                                               )
                                       )
                                       .entryTtl(Duration.ofDays(7L));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                                                         .cacheDefaults(redisCacheConfiguration)
                                                         .build();
    }
}
