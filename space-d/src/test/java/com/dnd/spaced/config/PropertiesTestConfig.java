package com.dnd.spaced.config;

import com.dnd.spaced.config.stub.StubNicknameProperties;
import com.dnd.spaced.global.config.properties.NicknameProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class PropertiesTestConfig {

    @Bean
    @Primary
    public NicknameProperties nicknameProperties() {
        return new StubNicknameProperties();
    }
}
