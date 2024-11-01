package com.dnd.spaced.config;

import com.dnd.spaced.config.stub.StubNicknameProperties;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public NicknameProperties nicknameProperties() {
        return new StubNicknameProperties();
    }
}
