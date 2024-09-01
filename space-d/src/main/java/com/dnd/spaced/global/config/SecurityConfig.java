package com.dnd.spaced.global.config;

import com.dnd.spaced.global.config.properties.NicknameProperties;
import com.dnd.spaced.global.config.properties.ProfileImageProperties;
import com.dnd.spaced.global.config.properties.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(
        value = {TokenProperties.class, NicknameProperties.class, ProfileImageProperties.class}
)
public class SecurityConfig {
}
