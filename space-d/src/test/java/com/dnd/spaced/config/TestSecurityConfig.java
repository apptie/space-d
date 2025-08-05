package com.dnd.spaced.config;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;

@Profile("test")
@Configuration
public class TestSecurityConfig {

    @MockBean
    JwtDecoderFactory<ClientRegistration> jwtDecoderFactory;
}
